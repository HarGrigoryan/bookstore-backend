package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "character", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"full_name","comment"})
        }
)
@Getter
@Setter
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_id_seq")
    @SequenceGenerator(name = "character_id_seq", sequenceName = "character_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "character")
    private List<BookCharacter> books = new ArrayList<>();

}
