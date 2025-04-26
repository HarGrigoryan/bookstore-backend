package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.service.dto.BookDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper implements Mapper<Book, BookDTO> {
    @Override
    public Book dtoToEntity(BookDTO dto) {
        if (dto == null) return null;
        Book book = new Book();
        book.setId(dto.getId());
        book.setBookId(dto.getBookId());
        book.setTitle(dto.getTitle());
        book.setPublisher(dto.getPublisher());
        book.setPublishDate(dto.getPublishDate());
        book.setFirstPublishDate(dto.getFirstPublishDate());
        book.setEdition(dto.getEdition());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());
        book.setFormat(dto.getFormat());
        book.setLanguage(dto.getLanguage());
        return book;

    }

    @Override
    public BookDTO entityToDto(Book entity) {
        BookDTO dto = new BookDTO();
        dto.setId(entity.getId());
        dto.setBookId(entity.getBookId());
        dto.setTitle(entity.getTitle());
        dto.setPublisher(entity.getPublisher());
        dto.setPublishDate(entity.getPublishDate());
        dto.setFirstPublishDate(entity.getFirstPublishDate());
        dto.setEdition(entity.getEdition());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setFormat(entity.getFormat());
        dto.setLanguage(entity.getLanguage());
        return dto;

    }

    @Override
    public List<BookDTO> entityToDto(Iterable<Book> entities) {
        return List.of();
    }

    @Override
    public List<Book> dtoToEntity(Iterable<BookDTO> dtos) {
        return List.of();
    }
}
