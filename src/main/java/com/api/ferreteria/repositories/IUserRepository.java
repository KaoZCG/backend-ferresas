package com.api.ferreteria.repositories;

import com.api.ferreteria.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;


@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
    List<UserModel> findByStatus(UserModel.UserStatus status);
    List<UserModel> findByRole(String role);
}