package com.example.bookstore.service;

import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.persistance.entity.Character;
import com.example.bookstore.persistance.repository.CharacterRepository;
import com.example.bookstore.service.dto.CharacterDTO;
import com.example.bookstore.service.dto.CharacterRequestDTO;
import com.example.bookstore.service.mapper.CharacterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;


    public CharacterDTO getById(Long id) {
        return characterMapper.entityToDto(characterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Character", id)));
    }

    public CharacterDTO updateById(Long id,  CharacterRequestDTO characterRequestDTO) {
        Character character = characterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Character", id));
        character.setFullName(characterRequestDTO.getFullName());
        character.setComment(characterRequestDTO.getComment());
        return characterMapper.entityToDto(characterRepository.save(character));
    }

    public CharacterDTO create(CharacterRequestDTO characterRequestDTO) {
        String fullName = characterRequestDTO.getFullName();
        String comment = characterRequestDTO.getComment();
        Character character = characterRepository.findByFullNameAndComment(fullName, comment);
        if (character != null) {
            throw new EntityAlreadyExistsException("Character", "name '%s'".formatted(fullName), "comment '%s'".formatted(comment));
        }
        character = new Character();
        character.setFullName(fullName);
        character.setComment(comment);
        return characterMapper.entityToDto(characterRepository.save(character));
    }

    public void deleteById(Long id) {
        Character character = characterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Character", id));
        List<Long> dependentBookIds = characterRepository.dependentBookIds(id);
        if(!dependentBookIds.isEmpty())
            throw new EntityDeletionException("Character", id, dependentBookIds);
        characterRepository.delete(character);
    }
}
