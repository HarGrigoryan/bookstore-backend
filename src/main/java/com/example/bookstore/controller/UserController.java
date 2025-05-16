package com.example.bookstore.controller;

import com.example.bookstore.enums.RoleName;
import com.example.bookstore.service.UserService;
import com.example.bookstore.service.criteria.UserSearchCriteria;
import com.example.bookstore.service.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasPermission('ANY', 'CREATE_ACCOUNT')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userCreateDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public PageResponseDTO<UserSearchResponseDTO> getAllUsers(UserSearchCriteria criteria) {
        return userService.getAllUsers(criteria);
    }

    @GetMapping("/{id}")
    @PreAuthorize("authentication.principal.userId == #id OR hasRole('STAFF')")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PreAuthorize("authentication.principal.userId == #id OR hasPermission('ROLE_STAFF', 'UPDATE_ACCOUNT') OR hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO updateDto) {
        return ResponseEntity.ok(userService.updateUser(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.principal.userId == #id OR hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_ACCOUNT')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    @PreAuthorize("authentication.principal.userId == #id")
    public ResponseEntity<UserDTO> changePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) {
        return ResponseEntity.ok(userService.changePassword(id, userPasswordChangeRequestDTO));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/add-roles")
    public ResponseEntity<Void> addRole(@PathVariable Long id, @RequestBody List<RoleName> roleNames) {
        userService.addRole(id, roleNames);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/revoke-roles")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> revokeRoles(@PathVariable Long id, @RequestBody List<RoleName> roleNames) {
        userService.revokeRole(id, roleNames);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/add-permissions")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> addPermissions(@PathVariable Long id, @RequestBody PermissionUpdateRequestDTO permissionUpdateRequestDTO) {
        userService.addPermissions(id, permissionUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/revoke-permissions")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> revokePermissions(@PathVariable Long id, @RequestBody PermissionUpdateRequestDTO permissionUpdateRequestDTO) {
        userService.revokePermissions(id, permissionUpdateRequestDTO);
        return ResponseEntity.noContent().build();
    }

}

