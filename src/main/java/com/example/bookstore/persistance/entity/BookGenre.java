package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="book_genre", uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "genre_id"})})
@Getter
@Setter
public class BookGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_genre_id_seq")
    @SequenceGenerator(name = "book_genre_id_seq", sequenceName = "book_genre_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="genre_id")
    private Genre genre;


}
