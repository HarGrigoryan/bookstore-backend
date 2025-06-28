package com.example.bookstore.persistance.entity;

import com.example.bookstore.enums.PictureSize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="book_cover_image")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookCoverImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_cover_image_id_seq")
    @SequenceGenerator(name = "book_cover_image_id_seq", sequenceName = "book_cover_image_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "file_information_id")
    private FileInformation fileInformation;

    @Column(name = "picture_size")
    @Enumerated(EnumType.STRING)
    private PictureSize pictureSize;

    public static BookCoverImage from(FileInformation coverImg, Book book) {
        BookCoverImage bookCoverImage = new BookCoverImage();
        bookCoverImage.setFileInformation(coverImg);
        bookCoverImage.setBook(book);
        bookCoverImage.setPictureSize(PictureSize.ORIGINAL);
        return bookCoverImage;
    }
}
