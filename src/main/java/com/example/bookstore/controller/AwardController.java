package com.example.bookstore.controller;

import com.example.bookstore.service.AwardService;
import com.example.bookstore.service.dto.AwardDTO;
import com.example.bookstore.service.dto.AwardRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public ResponseEntity<List<AwardDTO>> getAllAwards() {
        return ResponseEntity.ok(awardService.getAllAwards());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public ResponseEntity<AwardDTO> getAward(@PathVariable Long id) {
        return ResponseEntity.ok(awardService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<AwardDTO> updateAward(@PathVariable Long id, @RequestBody AwardRequestDTO requestDTO) {
        return ResponseEntity.ok(awardService.updateAwardById(id, requestDTO));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public ResponseEntity<AwardDTO> createAward(@RequestBody AwardRequestDTO requestDTO) {
        return ResponseEntity.ok(awardService.createAward(requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_INFORMATION')")
    public ResponseEntity<Void> deleteAward(@PathVariable Long id) {
        awardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
