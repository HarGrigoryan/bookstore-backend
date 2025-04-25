package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="book_character", uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "character_id"})})
@Getter
@Setter
public class BookCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_character_id_seq")
    @SequenceGenerator(name = "book_character_id_seq", sequenceName = "book_character_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="character_id")
    private Character character;

}
