package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.service.dto.BookCreateResponseDto;

public class BookCreateResponseDtoMapper {

    public BookCreateResponseDto map(Book book){
        BookCreateResponseDto bookCreateResponseDto = new BookCreateResponseDto();
        bookCreateResponseDto.setTitle(book.getTitle());
        bookCreateResponseDto.setDescription(book.getDescription());
        bookCreateResponseDto.setPublishDate(book.getPublishDate());
        bookCreateResponseDto.setFirstPublishDate(book.getFirstPublishDate());
        bookCreateResponseDto.setBookId(book.getBookId());
        bookCreateResponseDto.setIsbn(book.getIsbn());
        bookCreateResponseDto.setEdition(book.getEdition());
        bookCreateResponseDto.setPageNumber(book.getPageNumber());
        bookCreateResponseDto.setPrice(book.getPrice());
        bookCreateResponseDto.setId(book.getId());
        bookCreateResponseDto.setFormat(book.getFormat());
        return bookCreateResponseDto;
    }

}
