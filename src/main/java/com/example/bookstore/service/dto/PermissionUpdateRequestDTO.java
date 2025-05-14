package com.example.bookstore.service.dto;

import com.example.bookstore.enums.PermissionName;
import com.example.bookstore.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionUpdateRequestDTO {

    private RoleName roleName;
    private List<PermissionName> permissionNames;

}
