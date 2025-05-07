package com.example.bookstore.persistance.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.example.bookstore.enums.RoleName;

@Entity
@Table(name = "role")
@Setter
@Getter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

}
