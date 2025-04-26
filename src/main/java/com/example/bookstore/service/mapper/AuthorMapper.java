package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Author;
import com.example.bookstore.service.dto.AuthorDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper implements Mapper<Author, AuthorDTO> {
    @Override
    public Author dtoToEntity(AuthorDTO dto) {
        if(dto == null) return null;
        Author author = new Author();
        author.setFullName(dto.getFullName());
        author.setIsOnGoodreads(dto.getIsOnGoodreads());
        author.setId(dto.getId());
        return author;
    }

    @Override
    public AuthorDTO entityToDto(Author entity) {
        if(entity == null) return null;
        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setFullName(entity.getFullName());
        authorDto.setIsOnGoodreads(entity.getIsOnGoodreads());
        authorDto.setId(entity.getId());
        return authorDto;
    }

    @Override
    public List<AuthorDTO> entityToDto(Iterable<Author> entities) {
        List<AuthorDTO> authorDTOS = new ArrayList<>();
        for(Author author : entities) {
            authorDTOS.add(entityToDto(author));
        }
        return authorDTOS;
    }

    @Override
    public List<Author> dtoToEntity(Iterable<AuthorDTO> dtos) {
        List<Author> authors = new ArrayList<>();
        for(AuthorDTO authorDto : dtos) {
            authors.add(dtoToEntity(authorDto));
        }
        return authors;
    }
}
