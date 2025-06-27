package com.api.ferreteria.repositories;

import com.api.ferreteria.models.ModeratorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IModeratorRepository extends JpaRepository<ModeratorModel, Long> {
    ModeratorModel findByEmail(String email);
}
