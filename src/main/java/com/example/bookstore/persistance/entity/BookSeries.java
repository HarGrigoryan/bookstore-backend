package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="book_series", uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "series_id", "series_number"})})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookSeries {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_series_id_seq")
    @SequenceGenerator(name = "book_series_id_seq", sequenceName = "book_series_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="series_id")
    private Series series;

    @Column(name="series_number")
    private String seriesNumber;
}
