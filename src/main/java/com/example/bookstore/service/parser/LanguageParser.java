package com.example.bookstore.service.parser;

import com.example.bookstore.service.dto.LanguageDTO;

public class LanguageParser {

    public LanguageDTO parseLanguage(String languageString) {
        languageString = languageString.trim();
        if(languageString.isEmpty()) {return null;}
        LanguageDTO languageDto = new LanguageDTO();
        languageDto.setLanguage(languageString);
        return languageDto;
    }

}
