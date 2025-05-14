package com.example.bookstore.service;


import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.persistance.entity.Author;
import com.example.bookstore.persistance.repository.AuthorRepository;
import com.example.bookstore.service.criteria.AuthorSearchCriteria;
import com.example.bookstore.service.dto.AuthorCreateDTO;
import com.example.bookstore.service.dto.AuthorDTO;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.service.dto.PageResponseDTO;
import com.example.bookstore.service.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDTO> getTopPublishedAuthors(Integer top)
    {
        return authorMapper.entityToDto(authorRepository.findTopPublishedAuthors(top));
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null)
            throw new EntityNotFoundException("Author", id);
        return authorMapper.entityToDto(author);
    }


    public Integer getNumberOfPublishedBooksById(Long id) {
        return authorRepository.getNumberOfPublishedBooksById(id);
    }

    public AuthorDTO updateAuthor(AuthorDTO authorDto) {
        Author author = authorRepository.findById(authorDto.getId()).orElse(null);
        if(author == null)
            throw new EntityNotFoundException("Author", authorDto.getId());
        author.setFullName(authorDto.getFullName());
        author.setIsOnGoodreads(authorDto.getIsOnGoodreads());
        authorRepository.save(author);
        return authorMapper.entityToDto(author);
    }

    public AuthorDTO createAuthor(AuthorCreateDTO authorDto) {
        Author author = authorRepository.findAuthorByFullName(authorDto.getFullName()).orElse(null);
        if(author != null)
            throw new EntityAlreadyExistsException("Author", "full name '%s'".formatted(authorDto.getFullName()));
        author = new Author();
        author.setFullName(authorDto.getFullName());
        author.setIsOnGoodreads(authorDto.getIsOnGoodreads());
        return authorMapper.entityToDto(authorRepository.save(author));
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findAuthorById(id).orElseThrow(() -> new EntityNotFoundException("Author", id));
        List<Long> dependentBookIds = authorRepository.publishedBookIds(id);
        if(!dependentBookIds.isEmpty())
            throw new EntityDeletionException("Author", id, dependentBookIds);
        authorRepository.delete(author);
    }


    public PageResponseDTO<AuthorDTO> getAuthors(AuthorSearchCriteria authorSearchCriteria) {
        Page<AuthorDTO> page = authorRepository.findAll(authorSearchCriteria, authorSearchCriteria.buildPageRequest());
        return PageResponseDTO.from(page);
    }
}
