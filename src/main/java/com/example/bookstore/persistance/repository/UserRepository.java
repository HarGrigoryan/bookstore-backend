package com.example.bookstore.persistance.repository;

import com.example.bookstore.enums.RoleName;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

}
