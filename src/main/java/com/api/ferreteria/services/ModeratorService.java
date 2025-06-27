package com.api.ferreteria.services;

import com.api.ferreteria.models.ModeratorModel;
import com.api.ferreteria.repositories.IModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModeratorService {

    @Autowired
    private IModeratorRepository moderatorRepository;

    public List<ModeratorModel> getAllModerators() {
        return moderatorRepository.findAll();
    }

    public ModeratorModel saveModerator(ModeratorModel moderator) {
        return moderatorRepository.save(moderator);
    }

    public Optional<ModeratorModel> getModeratorById(Long id) {
        return moderatorRepository.findById(id);
    }

    public ModeratorModel updateModerator(ModeratorModel moderator, Long id) {
        ModeratorModel existingModerator = moderatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moderator not found"));

        existingModerator.setName(moderator.getName());
        existingModerator.setEmail(moderator.getEmail());
        existingModerator.setRole(moderator.getRole());
        existingModerator.setIsActive(moderator.getIsActive());

        return moderatorRepository.save(existingModerator);
    }

    public void deleteModerator(Long id) {
        moderatorRepository.deleteById(id);
    }
}