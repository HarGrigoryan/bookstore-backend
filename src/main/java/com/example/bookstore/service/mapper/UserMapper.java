package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.entity.UserRole;
import com.example.bookstore.persistance.entity.UserRolePermission;
import com.example.bookstore.persistance.repository.UserRolePermissionRepository;
import com.example.bookstore.persistance.repository.UserRoleRepository;
import com.example.bookstore.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserMapper implements Mapper<User, UserDTO> {

    private final UserRolePermissionRepository userRolePermissionRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public User dtoToEntity(UserDTO dto) {
        return null;
    }

    @Override
    public UserDTO entityToDto(User entity) {
        Long id = entity.getId();
        List<UserRolePermission> userRolePermissions = userRolePermissionRepository.findByUserId(id);
        List<UserRole> userRoles = userRoleRepository.findByUserId(id);
        return UserDTO.builder()
                .id(id)
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .email(entity.getEmail())
                .createdAt(LocalDate.ofInstant(entity.getCreatedAt(), ZoneId.systemDefault()))
                .updatedAt(LocalDate.ofInstant(entity.getUpdatedAt(), ZoneId.systemDefault()))
                .enabled(entity.isEnabled())
                .roles(userRoles.stream().map(ur -> ur.getRole().getName()).collect(Collectors.toList()))
                .permissions(
                        userRolePermissions.stream().collect(
                                Collectors.groupingBy(
                                        (UserRolePermission urp) -> urp.getUserRole().getRole().getName(),
                                        Collectors.mapping(
                                                (UserRolePermission urp) -> urp.getPermission().getName(),
                                                Collectors.toList()
                                        )
                                )
                        )
                )

                .build();
    }

    @Override
    public List<UserDTO> entityToDto(Iterable<User> entities) {
        List<UserDTO> userDTOS = new ArrayList<>();
         entities.forEach( entity -> userDTOS.add(entityToDto(entity))
         );
         return userDTOS;
    }

    @Override
    public List<User> dtoToEntity(Iterable<UserDTO> dtos) {
        return List.of();
    }
}
