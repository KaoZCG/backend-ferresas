package com.api.ferreteria.services;

import com.api.ferreteria.models.UserModel;
import com.api.ferreteria.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    // Métodos básicos CRUD
    public ArrayList<UserModel> getUsers() {
        return (ArrayList<UserModel>) userRepository.findAll();
    }

    public UserModel saveUser(UserModel user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<UserModel> getById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel updateById(UserModel request, Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(request.getName());
        user.setLast_name(request.getLast_name());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        // No actualizamos createdAt ni role por seguridad

        return userRepository.save(user);
    }

    public Boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<UserModel> saveUsers(List<UserModel> users) {
        return userRepository.saveAll(users);
    }

    // Métodos de moderación
    public UserModel blockUser(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setBlocked(true);
        user.setStatus(UserModel.UserStatus.SUSPENDED);
        return userRepository.save(user);
    }

    public UserModel unblockUser(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setBlocked(false);
        user.setStatus(UserModel.UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    public UserModel addWarning(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setWarningCount(user.getWarningCount() + 1);

        if (user.getWarningCount() >= 3) {
            user.setBlocked(true);
            user.setStatus(UserModel.UserStatus.BANNED);
        }

        return userRepository.save(user);
    }

    public UserModel updateUserStatus(Long id, UserModel.UserStatus status) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setStatus(status);
        return userRepository.save(user);
    }

    public void updateLastLogin(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserModel authenticateUser(String email, String password) {
        Optional<UserModel> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        UserModel user = userOptional.get();

        // Verificar si el usuario está bloqueado
        if (user.isBlocked()) {
            throw new RuntimeException("Usuario bloqueado");
        }

        // Verificar contraseña (en producción deberías usar BCrypt)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Actualizar último login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }

    public UserModel registerUser(UserModel newUser) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Hashear la contraseña (si implementaste BCrypt)
        // String hashedPassword = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
        // newUser.setPassword(hashedPassword);

        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setRole("USER");
        newUser.setStatus(UserModel.UserStatus.ACTIVE);

        return userRepository.save(newUser);
    }
}