package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_seq")
    @SequenceGenerator(name = "reviews_id_seq", sequenceName = "reviews_id_seq", allocationSize = 50)
    private Long id;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Book book;

    @Column(name = "rating", columnDefinition = "REAL CHECK (rating BETWEEN 0 AND 5)")
    private Float rating;

    @Column(name = "liked_percent", columnDefinition = "REAL CHECK (liked_percent BETWEEN 0 and 100)")
    private Float likedPercent;

    @Column(name = "bbe_score")
    private Integer bbeScore;

    @Column(name = "bbe_votes")
    private Integer bbeVotes;


}
