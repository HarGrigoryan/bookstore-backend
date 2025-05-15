package com.example.bookstore.persistance.repository;

import com.example.bookstore.enums.RoleName;
import com.example.bookstore.persistance.entity.Role;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("""
    SELECT ur FROM UserRole ur
    WHERE ur.user = :user
    AND ur.role = :role
""")
    UserRole findByUserAndRole(User user, Role role);

    @Query(
            """
        SELECT ur FROM UserRole ur
        WHERE ur.user.id = :userId
        AND ur.role.name = :roleName
        """
    )
    Optional<UserRole> findByUserIdAndRoleName(Long userId, RoleName roleName);
}
