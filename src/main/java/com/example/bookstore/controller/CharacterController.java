package com.example.bookstore.controller;

import com.example.bookstore.service.CharacterService;
import com.example.bookstore.service.dto.CharacterDTO;
import com.example.bookstore.service.dto.CharacterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        return ResponseEntity.ok(characterService.getAllCharacters());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public ResponseEntity<CharacterDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<CharacterDTO> updateCharacter(@PathVariable Long id, @RequestBody @Valid CharacterRequestDTO characterRequestDTO) {
        return ResponseEntity.ok(characterService.updateById(id, characterRequestDTO));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public ResponseEntity<CharacterDTO> createCharacter(@RequestBody @Valid CharacterRequestDTO characterRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.create(characterRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_INFORMATION')")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
