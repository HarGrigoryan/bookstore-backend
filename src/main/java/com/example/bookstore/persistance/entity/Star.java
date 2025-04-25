package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "star")
@Getter
@Setter
public class Star {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "star_id_seq")
    @SequenceGenerator(name = "star_id_seq", sequenceName = "star_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "star_number", unique = true, nullable = false)
    private Integer starNumber;
}
