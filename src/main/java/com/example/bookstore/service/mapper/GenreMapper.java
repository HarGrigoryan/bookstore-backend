package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Genre;
import com.example.bookstore.service.dto.GenreDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreMapper implements Mapper<Genre, GenreDTO> {

    @Override
    public Genre dtoToEntity(GenreDTO dto) {
        if (dto == null) {
            return null;
        }
        Genre genre = new Genre();
        genre.setId(dto.getId());
        genre.setName(dto.getName());
        return genre;
    }

    @Override
    public GenreDTO entityToDto(Genre entity) {
        if (entity == null) {
            return null;
        }
        GenreDTO genreDto = new GenreDTO();
        genreDto.setId(entity.getId());
        genreDto.setName(entity.getName());
        return genreDto;
    }

    @Override
    public List<GenreDTO> entityToDto(Iterable<Genre> entities) {
        List<GenreDTO> genreDTOS = new ArrayList<>();
        for (Genre genre : entities) {
            genreDTOS.add(entityToDto(genre));
        }
        return genreDTOS;
    }

    @Override
    public List<Genre> dtoToEntity(Iterable<GenreDTO> dtos) {
        List<Genre> genreDTOs = new ArrayList<>();
        for (GenreDTO genreDto : dtos) {
            genreDTOs.add(dtoToEntity(genreDto));
        }
        return genreDTOs;
    }
}
