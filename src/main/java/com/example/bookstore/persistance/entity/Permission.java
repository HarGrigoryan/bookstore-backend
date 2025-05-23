package com.example.bookstore.persistance.entity;

import com.example.bookstore.enums.PermissionName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "permission")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private PermissionName name;

    @OneToMany(mappedBy = "permission", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<UserRolePermission> userRolePermissions;


}
