package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.bookstore.enums.Format;

@Entity
@Table(name="book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_sequence")
    @SequenceGenerator(name = "book_id_sequence", sequenceName = "book_id_sequence", allocationSize = 50)
    private Long id;

    @Column(name="book_id", nullable=false, unique = true)
    private String bookId;

    @Column(name = "title",nullable=false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "first_publish_date")
    private LocalDate firstPublishDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "format")
    @Enumerated(value = EnumType.STRING)
    private Format format;

    @Column(name = "isbn")
    private String isbn;

    @JoinColumn(name = "language_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;

    @Column(name = "edition")
    private String edition;

    @Column(name="page_number")
    private Integer pageNumber;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "book", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private List<BookInstance> copies = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<BookGenre> genres = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<BookSetting> bookSettingList = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<BookSeries> bookSeriesList = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<BookCharacter> bookCharacterList = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<BookAuthor> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<BookAward> awards = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<RatingByStars> ratingsByStars = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<BookCoverImage> coverImages = new ArrayList<>();

    public void addRatingByStars(Star star, Integer starNumber, Integer totalNumberOfRatings) {
        RatingByStars ratingByStars = new RatingByStars();
        ratingByStars.setStar(star);
        ratingByStars.setNumberOfRatings(starNumber);
        ratingByStars.setTotalNumberOfRatings(totalNumberOfRatings);
        ratingByStars.setBook(this);
        ratingsByStars.add(ratingByStars);
    }

}
