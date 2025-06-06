package com.example.bookstore.controller;

import com.example.bookstore.service.SettingService;
import com.example.bookstore.service.dto.SettingDTO;
import com.example.bookstore.service.dto.SettingRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'USER')")
    public SettingDTO getById(@PathVariable Long id) {
        return settingService.getById(id);
    }

    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("{id}")
    public SettingDTO updateById(@PathVariable Long id, @RequestBody @Valid SettingRequestDTO settingDTO) {
        return settingService.updateById(id, settingDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public SettingDTO create(@RequestBody @Valid SettingRequestDTO settingDTO) {
        return settingService.createSetting(settingDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_INFORMATION')")
    public void delete(@PathVariable Long id) {
        settingService.deleteById(id);
    }

}
