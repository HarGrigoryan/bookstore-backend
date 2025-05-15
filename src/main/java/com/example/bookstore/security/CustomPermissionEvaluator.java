package com.example.bookstore.security;

import com.example.bookstore.enums.PermissionName;
import com.example.bookstore.enums.RoleName;
import com.example.bookstore.persistance.entity.UserRolePermission;
import com.example.bookstore.persistance.repository.UserRolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("CustomPermissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final UserRolePermissionRepository userRolePermissionRepository;


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long userId = principal.getUserId();
        PermissionName permissionName = PermissionName.valueOf(permission.toString());
        RoleName roleName;
        if(targetDomainObject.toString().equalsIgnoreCase("ANY"))
            roleName = null;
        else
            roleName = RoleName.valueOf(targetDomainObject.toString());
        UserRolePermission userRolePermission = userRolePermissionRepository
                .findByUserIdAndRoleNameAndPermissionName(userId, permissionName, roleName)
                .orElse(null);
        return userRolePermission != null;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long userId = principal.getUserId();
        PermissionName permissionName = PermissionName.valueOf(permission.toString());
        RoleName roleName = RoleName.valueOf(targetType);
        UserRolePermission userRolePermission = userRolePermissionRepository.findByUserIdAndRoleNameAndPermissionName(userId,
                permissionName, roleName).orElse(null);
        return userRolePermission != null;
    }
}
