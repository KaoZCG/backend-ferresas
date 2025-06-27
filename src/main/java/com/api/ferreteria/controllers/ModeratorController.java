package com.api.ferreteria.controllers;

import com.api.ferreteria.models.ModeratorModel;
import com.api.ferreteria.services.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderators")
public class ModeratorController {

    @Autowired
    private ModeratorService moderatorService;

    @GetMapping
    public List<ModeratorModel> getAllModerators() {
        return moderatorService.getAllModerators();
    }

    @PostMapping
    public ModeratorModel createModerator(@RequestBody ModeratorModel moderator) {
        return moderatorService.saveModerator(moderator);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeratorModel> getModeratorById(@PathVariable Long id) {
        return moderatorService.getModeratorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ModeratorModel updateModerator(@RequestBody ModeratorModel moderator, @PathVariable Long id) {
        return moderatorService.updateModerator(moderator, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModerator(@PathVariable Long id) {
        moderatorService.deleteModerator(id);
        return ResponseEntity.ok().build();
    }
}