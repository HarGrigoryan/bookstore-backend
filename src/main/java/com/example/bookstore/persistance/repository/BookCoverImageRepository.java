package com.example.bookstore.persistance.repository;

import com.example.bookstore.enums.PictureSize;
import com.example.bookstore.persistance.entity.BookCoverImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCoverImageRepository extends JpaRepository<BookCoverImage, Long> {

    @Query("""
            SELECT f.filePath
            FROM BookCoverImage b
            JOIN FileInformation f ON b.fileInformation = f
            WHERE b.book.id = :bookId
            AND b.pictureSize = :pictureSize""")
    String getImage(Long bookId, PictureSize pictureSize);
}
