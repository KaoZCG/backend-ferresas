package com.api.ferreteria.controllers;

import com.api.ferreteria.models.UserModel;
import com.api.ferreteria.repositories.IUserRepository;
import com.api.ferreteria.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IUserRepository userRepository;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserModel>> getUsers() {
        try {
            ArrayList<UserModel> users = userService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un usuario (registro)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userData) {
        try {
            // Validación básica
            if (userData.get("email") == null || userData.get("password") == null ||
                    userData.get("name") == null || userData.get("last_name") == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Todos los campos son requeridos: email, password, name, last_name"
                ));
            }

            // Crear nuevo usuario
            UserModel newUser = new UserModel();
            newUser.setEmail(userData.get("email"));
            newUser.setPassword(userData.get("password")); // Se hashea en el servicio si usas BCrypt
            newUser.setName(userData.get("name"));
            newUser.setLast_name(userData.get("last_name"));

            // Campos opcionales
            if (userData.containsKey("phoneNumber")) {
                newUser.setPhoneNumber(userData.get("phoneNumber"));
            }

            UserModel savedUser = userService.registerUser(newUser);

            // Preparar respuesta
            Map<String, Object> response = Map.of(
                    "id", savedUser.getId(),
                    "name", savedUser.getName(),
                    "email", savedUser.getEmail(),
                    "role", savedUser.getRole()
            );

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar el usuario"));
        }
    }

    // Obtener perfil de usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserProfile(@PathVariable Long id) {
        return userService.getById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Actualizar perfil de usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateProfile(@PathVariable Long id, @RequestBody UserModel user) {
        try {
            // Aseguramos que no se pueda cambiar el rol desde aquí
            user.setRole("USER");
            UserModel updatedUser = userService.updateById(user, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar cuenta de usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAccount(@PathVariable Long id) {
        try {
            if (userService.deleteUser(id)) {
                return ResponseEntity.ok(Map.of("message", "Cuenta eliminada exitosamente"));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Error al eliminar la cuenta"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar contraseña
    @PutMapping("/{id}/password")
    public ResponseEntity<Map<String, String>> updatePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {
        try {
            String newPassword = passwords.get("newPassword");
            UserModel user = userService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            user.setPassword(newPassword); // En una implementación real, esto debería estar hasheado
            userService.saveUser(user);

            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada exitosamente"));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", "Usuario no encontrado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Error al actualizar la contraseña"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar información de contacto
    @PutMapping("/{id}/contact")
    public ResponseEntity<UserModel> updateContactInfo(
            @PathVariable Long id,
            @RequestBody Map<String, String> contactInfo) {
        try {
            UserModel user = userService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (contactInfo.containsKey("email")) {
                user.setEmail(contactInfo.get("email"));
            }
            if (contactInfo.containsKey("phoneNumber")) {
                user.setPhoneNumber(contactInfo.get("phoneNumber"));
            }

            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email y contraseña son requeridos"));
            }

            UserModel user = userService.authenticateUser(email, password);

            // Crear respuesta con los datos necesarios
            // No enviar la contraseña por seguridad
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en el servidor"));
        }
    }
}