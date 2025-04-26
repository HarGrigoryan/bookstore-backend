package com.example.bookstore.service.parser;

import com.example.bookstore.service.dto.GenreDTO;

import java.util.*;

public class GenresParser {

    public List<GenreDTO> parseGenres(String genres) {
        List<GenreDTO> genresList = new ArrayList<>();
        Set<String> stringGenresList = genresToStringParser(genres);
        for (String genre : stringGenresList) {
            if (!genre.isEmpty()) {
                GenreDTO finalGenre = new GenreDTO();
                finalGenre.setName(genre.trim());
                genresList.add(finalGenre);
            }
        }
        return genresList;
    }

    public Set<String> genresToStringParser(String genres) {
        genres = genres.trim();
        genres = genres.substring(1, genres.length()-1).trim();
        String[] genresArray = genres.split("(?<=['\"]),");
        Set<String> genresList = new HashSet<>();
        for (String genre : genresArray) {
            if(!genre.isEmpty()) {
                genre = genre.trim();
                genresList.add(genre.substring(1, genre.length() - 1).trim());
            }

        }
        return genresList;
    }

}
