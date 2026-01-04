package com.example.bookstore.service.dto;

import com.example.bookstore.enums.PermissionName;
import com.example.bookstore.enums.RoleName;
import com.example.bookstore.persistance.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Boolean enabled;
    private List<RoleName> roles;
    private Map<RoleName, List<PermissionName>> permissions;

    public static UserDTO toSimpleDTO(User user) {
        final UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(LocalDate.ofInstant(user.getCreatedAt(), ZoneId.systemDefault()));
        userDTO.setUpdatedAt(LocalDate.ofInstant(user.getUpdatedAt(), ZoneId.systemDefault()));
        userDTO.setEnabled(user.isEnabled());
        return userDTO;
    }
}
