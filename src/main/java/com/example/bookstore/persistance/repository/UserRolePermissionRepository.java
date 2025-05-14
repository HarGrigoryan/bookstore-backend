package com.example.bookstore.persistance.repository;

import com.example.bookstore.enums.PermissionName;
import com.example.bookstore.enums.RoleName;
import com.example.bookstore.persistance.entity.UserRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, Long> {

    @Query("""
        SELECT urp FROM UserRolePermission urp
        WHERE urp.userRole.user.id = :userId
        AND urp.permission.name = :permissionName
        AND urp.userRole.role.name = :roleName
        """)
    Optional<UserRolePermission> findByUserIdAndRoleNameAndPermissionName(@Param("userId") Long userId, @Param("permissionName") PermissionName permissionName, @Param("roleName") RoleName roleName );

}
