package com.example.bookstore.service.parser;

import com.example.bookstore.service.dto.CharacterDTO;

import java.util.*;

public class CharactersParser {

    public Set<CharacterDTO> parseCharacter(String characterString) {
        Set<CharacterDTO> characterDTOS = new HashSet<>();
        String[] records = characterString.substring(1, characterString.length()-1).split(",(?![^()]*\\))");
        for (String record : records) {
            if(record.trim().length() > 1) {
                String cleanRecord = record.trim();
                cleanRecord = cleanRecord.replaceAll("(?<!\\w)'|'(?!\\w)", "").trim();
                if (!cleanRecord.isEmpty()) {
                    CharacterDTO characterDto = getCharacterDto(cleanRecord);
                    characterDTOS.add(characterDto);
                }

            }
        }
        return characterDTOS;
    }

    private static CharacterDTO getCharacterDto(String cleanRecord) {
        CharacterDTO characterDto = new CharacterDTO();
        if (cleanRecord.charAt(cleanRecord.length() - 1) == ')') {
                String[] cleanRecordArray = cleanRecord.split("\\(");
                characterDto.setFullName(cleanRecordArray[0].trim());
                characterDto.setComment(cleanRecordArray[1].substring(0, cleanRecordArray[1].length() - 1).trim().toLowerCase());
        }
        else {
            characterDto.setFullName(cleanRecord.trim());
        }
        return characterDto;
    }

    public Set<String> charactersToStringParser(String charactersString) {
        Set<String> charactersMap = new HashSet<>();
        Set<CharacterDTO> characterDTOS = parseCharacter(charactersString);
        for (CharacterDTO characterDto : characterDTOS) {
            String characterKey = characterDto.getFullName() + characterDto.getComment();
            charactersMap.add(characterKey);
        }
        return charactersMap;
    }


}
