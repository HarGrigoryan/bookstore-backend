package com.example.bookstore.service;

import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.persistance.entity.Language;
import com.example.bookstore.persistance.repository.LanguageRepository;
import com.example.bookstore.service.dto.LanguageDTO;
import com.example.bookstore.service.dto.LanguageRequestDTO;
import com.example.bookstore.service.mapper.LanguageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageDTO getById(Long id) {
        return languageMapper.entityToDto(languageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Language", id)));
    }


    public void delete(Long id) {
        Language language = languageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Language", id));
        try {
            languageRepository.delete(language);
        }catch (DataIntegrityViolationException e) {
            if(e.getMostSpecificCause().getMessage().contains("violates foreign key constraint")){
                List<Long> dependentBookIds = languageRepository.dependentBookIds(id);
                throw new EntityDeletionException("Language with id: '" + id +
                        "' could not be deleted successfully. Details: The books with the following ids %s are depended on the specified language.".formatted(dependentBookIds));
            }
            throw new EntityDeletionException(id, e.getMessage());
        }
    }

    public LanguageDTO updateById(Long id, LanguageRequestDTO languageDTO) {
        Language language = languageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Language with id '%s' not found.".formatted(id)));
        language.setLanguage(languageDTO.getName());
        return languageMapper.entityToDto(languageRepository.save(language));
    }

    public LanguageDTO create(@Valid LanguageRequestDTO languageDTO) {
        Language language = languageRepository.findByLanguage(languageDTO.getName());
        if(language != null){
            throw new EntityAlreadyExistsException("Language with name '%s' already exists.".formatted(languageDTO.getName()));
        }
        language = new Language();
        language.setLanguage(languageDTO.getName());
        return languageMapper.entityToDto(languageRepository.save(language));

    }
}
