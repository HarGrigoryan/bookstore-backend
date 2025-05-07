package com.example.bookstore.service.dto;

import com.example.bookstore.enums.Format;
import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.persistance.entity.Language;
import com.example.bookstore.persistance.entity.Publisher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookSearchResponseDTO {

    private Long id;

    private String bookId;

    private String title;

    private LocalDate publishDate;

    private LocalDate firstPublishDate;

    private String description;

    private Format format;

    private String isbn;

    private String edition;

    private Integer pageNumber;

    private BigDecimal price;

    private Long publisherId;

    private Long languageId;

    public static BookSearchResponseDTO mapToDTO(Book book) {
        BookSearchResponseDTO dto = new BookSearchResponseDTO();
        dto.setId(book.getId());
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setPublishDate(book.getPublishDate());
        dto.setFirstPublishDate(book.getFirstPublishDate());
        dto.setDescription(book.getDescription());
        dto.setFormat(book.getFormat());
        dto.setIsbn(book.getIsbn());
        dto.setEdition(book.getEdition());
        dto.setPageNumber(book.getPageNumber());
        dto.setPrice(book.getPrice());
        Publisher publisher = book.getPublisher();
        if(publisher != null)
            dto.setPublisherId(publisher.getId());
        else
            dto.setPublisherId(null);
        Language language = book.getLanguage();
        if(language != null)
            dto.setLanguageId(language.getId());
        else
            dto.setLanguageId(null);
        return dto;
    }

    public static List<BookSearchResponseDTO> mapToDTO(Collection<Book> books) {
        List<BookSearchResponseDTO> dtos = new ArrayList<>();
        for (Book book : books) {
            dtos.add(mapToDTO(book));
        }
        return dtos;
    }
}
