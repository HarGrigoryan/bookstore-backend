package com.example.bookstore.persistance.repository;

import com.example.bookstore.enums.PermissionName;
import com.example.bookstore.persistance.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(
        """
        SELECT p FROM Permission p WHERE p.name = :name
        """
    )
    Optional<Permission> findByName(PermissionName name);

}
