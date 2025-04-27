package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rating_by_star")
@Getter
@Setter
public class RatingByStars {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_by_star_id_seq")
    @SequenceGenerator(name = "rating_by_star_id_seq", sequenceName = "rating_by_star_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="star_id")
    private Star star;

    @Column(name="number_of_ratings")
    private Integer numberOfRatings;

    @Column(name = "total_number_of_ratings")
    private Integer totalNumberOfRatings;
}
