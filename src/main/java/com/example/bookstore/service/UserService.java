package com.example.bookstore.service;

import com.example.bookstore.enums.PermissionName;
import com.example.bookstore.enums.RoleName;
import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.persistance.entity.*;
import com.example.bookstore.persistance.repository.*;
import com.example.bookstore.service.criteria.UserSearchCriteria;
import com.example.bookstore.service.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.example.bookstore.exception.ResourceAlreadyUsedException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRolePermissionRepository userRolePermissionRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) {

        String email = userCreateDTO.getEmail().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyUsedException("User with email [%s] already exists".formatted(email));
        }

        List<Role> roles = userCreateDTO.getRoles().stream()
                .map(r -> roleRepository.findByName(r).orElseThrow(() -> new EntityNotFoundException("Role with name %s not found".formatted(r)))).toList();

        final User user = new User();
        user.setFirstname(userCreateDTO.getFirstname());
        user.setLastname(userCreateDTO.getLastname());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getTemporaryPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        roles.forEach(r -> assignRole(user, r));
        return UserDTO.toDTO(user);
    }

    public PageResponseDTO<UserSearchResponseDTO> getAllUsers(UserSearchCriteria criteria) {
        Page<UserSearchResponseDTO> page = userRepository.findAll(criteria, criteria.buildPageRequest());
        return PageResponseDTO.from(page);
    }

    public UserDTO getById(Long id) {

        final User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));

        return UserDTO.toDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO updateDto) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstname(updateDto.getFirstname());
        user.setLastname(updateDto.getLastname());
        user.setEnabled(updateDto.isEnabled());

        return UserDTO.toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.deleteById(id);
    }

    public UserDTO changePassword(Long id,  UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id '%s' not found".formatted(id)));
        user.setPassword(passwordEncoder.encode(userPasswordChangeRequestDTO.getNewPassword()));
        return UserDTO.toDTO(userRepository.save(user));
    }

    public void assignRole(User user, Role role)
    {
        UserRole userRole = userRoleRepository.findByUserAndRole(user, role);
        if(userRole != null)
            throw new EntityAlreadyExistsException("User with id [%s] already has the role [%s]".formatted(user.getId(), role.getName()));
        userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);
    }

    public void addRole(Long userId, List<RoleName> roleNames) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName).orElseThrow(() -> new EntityNotFoundException("Role with name %s not found".formatted(roleName))))
                .toList();
        roles.forEach(r -> assignRole(user, r));
    }

    public void addPermissions(Long id, PermissionUpdateRequestDTO permissionUpdateRequestDTO) {
        RoleName roleName = permissionUpdateRequestDTO.getRoleName();
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new EntityNotFoundException("Role with name '%s' not found".formatted(roleName)));
        UserRole userRole = userRepository.findRole(id, roleName);
        if(userRole == null) {
            throw new EntityNotFoundException("User with id '%s' does not have an assigned role of '%s'".formatted(id, roleName));
        }
        List<PermissionName> permissionNames = permissionUpdateRequestDTO.getPermissionNames();
        List<Permission> permissions = permissionNames.stream()
                .map(p -> permissionRepository.findByName(p).orElseThrow( () -> new EntityNotFoundException("Permission with permission name [%s] does not exist.")))
                .toList();
        List<UserRolePermission> userRolePermissions = permissions.stream()
                .map(p -> userRolePermissionRepository.findByUserIdAndRoleAndPermission(id, role, p).orElse(null))
                .toList();
        userRolePermissions.forEach(u-> {
            if(u != null)
                throw new EntityAlreadyExistsException("User with id [%s] already has permission [%s] defined for the role [%s]".formatted(id, u.getPermission().getName(), u.getUserRole().getRole().getName()));
        });
        permissions.forEach(permission -> {
            UserRolePermission userRolePermission = new UserRolePermission();
            userRolePermission.setPermission(permission);
            userRolePermission.setUserRole(userRole);
            userRolePermissionRepository.save(userRolePermission);
        });
    }

    public void revokeRole(Long userId, List<RoleName> roleNames) {
        List<UserRole> userRoles = roleNames
                .stream()
                .map(roleName -> userRoleRepository.findByUserIdAndRoleName(userId, roleName)
                        .orElseThrow(() ->
                                new EntityNotFoundException(("User with id [%s] does not have " +
                                        "the assigned role of '%s'.").formatted(userId, roleName))))
                .toList();
        userRoleRepository.deleteAll(userRoles);
    }

    public void revokePermissions(Long id, PermissionUpdateRequestDTO permissionUpdateRequestDTO) {
        RoleName roleName = permissionUpdateRequestDTO.getRoleName();
        List<UserRolePermission> userRolePermissions = permissionUpdateRequestDTO.getPermissionNames()
                .stream()
                .map(permissionName ->
                        userRolePermissionRepository.findByUserIdAndRoleNameAndPermissionName(id, permissionName, roleName)
                                .orElseThrow(() ->
                                        new EntityNotFoundException(("User with id [%s] " +
                                                "does not have permission [%s] defined for role [%s]")
                                                .formatted(id, permissionName, roleName))))
                .toList();
        userRolePermissionRepository.deleteAll(userRolePermissions);
    }
}
