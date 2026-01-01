package com.example.bookstore.service;

import com.example.bookstore.persistance.repository.GenreRepository;
import com.example.bookstore.service.dto.GenreDTO;
import com.example.bookstore.service.mapper.GenreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<GenreDTO> getAll() {
        return genreMapper.entityToDto(genreRepository.findAll()).stream()
                .sorted(Comparator.comparing(GenreDTO::getName))
                .toList();
    }

}
