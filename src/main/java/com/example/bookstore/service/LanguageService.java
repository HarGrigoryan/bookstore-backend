package com.example.bookstore.service;

import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.persistance.entity.Language;
import com.example.bookstore.persistance.repository.LanguageRepository;
import com.example.bookstore.service.dto.LanguageDTO;
import com.example.bookstore.service.dto.LanguageRequestDTO;
import com.example.bookstore.service.mapper.Mapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final Mapper<Language, LanguageDTO> languageMapper;

    public LanguageDTO getById(Long id) {
        return languageMapper.entityToDto(languageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Language", id)));
    }


    public void delete(Long id) {
        Language language = languageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Language", id));
        List<Long> dependentBookIds = languageRepository.dependentBookIds(id);
        if(!dependentBookIds.isEmpty())
            throw new EntityDeletionException("Language", id, dependentBookIds);
        languageRepository.delete(language);
    }

    public LanguageDTO updateById(Long id, LanguageRequestDTO languageDTO) {
        Language language = languageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Language with id '%s' not found.".formatted(id)));
        language.setLanguage(languageDTO.getName());
        return languageMapper.entityToDto(languageRepository.save(language));
    }

    public LanguageDTO create(@Valid LanguageRequestDTO languageDTO) {
        Language language = languageRepository.findByLanguage(languageDTO.getName());
        if(language != null){
            throw new EntityAlreadyExistsException("Language", "name '%s'".formatted(languageDTO.getName()));
        }
        language = new Language();
        language.setLanguage(languageDTO.getName());
        return languageMapper.entityToDto(languageRepository.save(language));

    }

    public List<LanguageDTO> getAll() {
        List<Language> languages = languageRepository.findAll();
        return languages.stream().sorted(Comparator.comparing(Language::getLanguage)).map(languageMapper::entityToDto).collect(Collectors.toList());
    }
}
