package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_award", uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "award_id"})})
@Setter
@Getter
public class BookAward {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_award_id_seq")
    @SequenceGenerator(name = "book_award_id_seq", sequenceName = "book_award_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="award_id")
    private Award award;


}
