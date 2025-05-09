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

@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
//TODO: Improve security
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/{id}")
    public ResponseEntity<CharacterDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.getById(id));
    }

    //TODO: A task for final touch-ups: ensure consistency for the resource naming of Put requests
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<CharacterDTO> updateCharacter(@PathVariable Long id, @RequestBody @Valid CharacterRequestDTO characterRequestDTO) {
        return ResponseEntity.ok(characterService.updateById(id, characterRequestDTO));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<CharacterDTO> createCharacter(@RequestBody @Valid CharacterRequestDTO characterRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.create(characterRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
