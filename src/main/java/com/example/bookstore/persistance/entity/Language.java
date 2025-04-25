package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "language")
@Getter
@Setter
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_id_seq")
    @SequenceGenerator(name = "language_id_seq", sequenceName = "language_id_seq", allocationSize = 50)
    private Long id;

    @Column(name ="language", nullable = false, unique = true)
    private String language;
}
