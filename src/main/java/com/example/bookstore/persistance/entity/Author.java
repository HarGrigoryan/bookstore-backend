package com.example.bookstore.persistance.entity;

import com.example.bookstore.enums.AuthorRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="author")
@Setter
@Getter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    @SequenceGenerator(name = "author_id_seq", sequenceName = "author_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "full_name", unique = true, nullable = false)
    private String fullName;

    @Column(name = "is_on_goodreads")
    private Boolean isOnGoodreads;

    @OneToMany(mappedBy ="author", cascade = CascadeType.PERSIST)
    private List<BookAuthor> books = new ArrayList<>();

}
