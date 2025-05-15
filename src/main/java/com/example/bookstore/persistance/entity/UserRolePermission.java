package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "user_role_permission", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_role_id","permission_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_role_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserRole userRole;

    @JoinColumn(name = "permission_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Permission permission;

}
