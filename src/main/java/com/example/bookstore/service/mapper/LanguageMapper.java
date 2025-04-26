package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Language;
import com.example.bookstore.service.dto.LanguageDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LanguageMapper implements Mapper<Language, LanguageDTO> {

    @Override
    public Language dtoToEntity(LanguageDTO languageDto) {
        if (languageDto == null) {
            return null;
        }
        Language language = new Language();
        language.setId(languageDto.getId());
        language.setLanguage(languageDto.getLanguage());
        return language;
    }

    @Override
    public LanguageDTO entityToDto(Language language) {
        if (language == null) {
            return null;
        }
        LanguageDTO languageDto = new LanguageDTO();
        languageDto.setId(language.getId());
        languageDto.setLanguage(language.getLanguage());
        return languageDto;
    }

    @Override
    public List<LanguageDTO> entityToDto(Iterable<Language> entities) {
        List<LanguageDTO> languageDTOS = new ArrayList<>();
        for (Language language : entities) {
            LanguageDTO languageDto = entityToDto(language);
            languageDTOS.add(languageDto);
        }
        return languageDTOS;
    }

    @Override
    public List<Language> dtoToEntity(Iterable<LanguageDTO> dtos) {
        List<Language> languages = new ArrayList<>();
        for (LanguageDTO languageDto : dtos) {
            Language language = dtoToEntity(languageDto);
            languages.add(language);
        }
        return languages;
    }


}
