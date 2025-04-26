package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Character;
import com.example.bookstore.service.dto.CharacterDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CharacterMapper implements Mapper<Character, CharacterDTO> {
    @Override
    public Character dtoToEntity(CharacterDTO dto) {
        if (dto == null) return null;
        Character character = new Character();
        character.setId(dto.getId());
        character.setFullName(dto.getFullName());
        character.setComment(dto.getComment());
        return character;
    }

    @Override
    public CharacterDTO entityToDto(Character entity) {
        if (entity == null) return null;
        CharacterDTO characterDto = new CharacterDTO();
        characterDto.setId(entity.getId());
        characterDto.setFullName(entity.getFullName());
        characterDto.setComment(entity.getComment());
        return characterDto;
    }

    @Override
    public List<CharacterDTO> entityToDto(Iterable<Character> entities) {
        List<CharacterDTO> characterDTOS = new ArrayList<>();
        for (Character entity : entities) {
            characterDTOS.add(entityToDto(entity));
        }
        return characterDTOS;
    }

    @Override
    public List<Character> dtoToEntity(Iterable<CharacterDTO> dtos) {
        List<Character> characters = new ArrayList<>();
        for (CharacterDTO dto : dtos) {
            characters.add(dtoToEntity(dto));
        }
        return characters;
    }
}
