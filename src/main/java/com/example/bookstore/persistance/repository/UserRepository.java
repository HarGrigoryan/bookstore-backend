package com.example.bookstore.persistance.repository;

import com.example.bookstore.enums.RoleName;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.entity.UserRole;
import com.example.bookstore.service.criteria.UserSearchCriteria;
import com.example.bookstore.service.dto.UserSearchResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("""
    SELECT ur.role.name FROM User u
    JOIN UserRole ur ON u = ur.user
    WHERE u = :user
""")
    List<String> findAuthorities(User user);

    @Query("""
        SELECT ur FROM UserRole ur
                WHERE ur.user.id = :userId
                AND ur.role.name = :roleName
        """
    )
    UserRole findRole(Long userId, RoleName roleName);

    @Query("""
    SELECT DISTINCT new com.example.bookstore.service.dto.UserSearchResponseDTO(
            u.id,
            u.firstname,
            u.lastname,
            u.email,
            u.enabled,
            u.createdAt,
            u.updatedAt
            )
    FROM User u
    LEFT JOIN u.roles ur
    LEFT JOIN ur.userRolePermissions urp
    ON (:#{#criteria.permissionName} IS NULL OR urp.permission.name = :#{#criteria.permissionName})
    WHERE (:#{#criteria.roleName} IS NULL OR ur.role.name = :#{#criteria.roleName})
    AND (:#{#criteria.firstname} IS NULL OR LOWER(u.firstname) LIKE CONCAT( '%', LOWER(:#{#criteria.firstname}), '%'))
    AND (:#{#criteria.lastname} IS NULL OR LOWER(u.lastname) LIKE CONCAT('%', LOWER(:#{#criteria.lastname}), '%'))
    AND (:#{#criteria.email} IS NULL OR LOWER(u.email) LIKE CONCAT('%', LOWER(:#{#criteria.email}), '%'))
    AND (:#{#criteria.enabled} IS NULL OR u.enabled = :#{#criteria.enabled})
    AND (u.createdAt >= :#{#criteria.createdAt})
    AND (u.updatedAt >= :#{#criteria.updatedAt})
    """)
    Page<UserSearchResponseDTO> findAll(
            @Param("criteria") UserSearchCriteria criteria,
            Pageable pageable
    );


    @Query("""
        SELECT u FROM User u
        LEFT JOIN u.roles ur
        WHERE ur.role.name = :roleName
        """)
    User findAnyByRoleName(RoleName roleName);
}
