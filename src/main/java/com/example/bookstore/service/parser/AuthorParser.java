package com.example.bookstore.service.parser;

import com.example.bookstore.enums.AuthorRole;
import com.example.bookstore.service.dto.AuthorDTO;

import java.util.*;

public class AuthorParser {

    public Set<AuthorDTO> parseAuthors(String authorsString)
    {
        Set<AuthorDTO> authorDTOS = new HashSet<>();
        String[] authors = authorsString.split(",(?![^()]*\\))");
        for(String authorString : authors)
        {
            authorString = authorString.trim();
            if(!authorString.isEmpty())
            {
                AuthorDTO authorDto = new AuthorDTO();
                String fullName = authorString;
                boolean isOnGoodReads  = false;
                if(authorString.contains("("))
                {
                    String[] authorStrings = authorString.split("\\(");
                    fullName = authorStrings[0].trim();
                    if(fullName.isEmpty())
                        continue;
                    for(String authorComponent : authorStrings)
                    {
                        if(authorComponent.contains("Goodreads Author")) {
                            isOnGoodReads = true;
                            break;
                        }

                    }
                }
                authorDto.setFullName(fullName);
                authorDto.setIsOnGoodreads(isOnGoodReads);
                authorDTOS.add(authorDto);
            }
        }
        return authorDTOS;
    }

    public Map<String, AuthorRole> parseAuthorsString(String authorsString)
    {
        Map<String, AuthorRole> authorsAndRolesMap = new HashMap<>();
        String[] potentialAuthors = authorsString.split(",(?![^()]*\\))");
        for(String authorString : potentialAuthors){
            authorString = authorString.trim();
            if(!authorString.isEmpty())
            {
                AuthorRole authorRole = AuthorRole.AUTHOR;
                String AuthorName = authorString;
                if(authorString.contains("("))
                {
                    String[] authorStrings = authorString.split("\\(");
                    AuthorName = authorStrings[0].trim();
                    for (int i = 1; i < authorStrings.length; i++) {
                        authorStrings[i] = authorStrings[i].trim().replaceAll("[()]", "");
                        if(!authorStrings[i].contains("Goodreads Author")) {
                            authorRole = (new AuthorRoleParser()).parseAuthorRole(authorStrings[i]);
                            break;
                        }

                    }
                }
                authorsAndRolesMap.put(AuthorName, authorRole);
            }
        }
        return authorsAndRolesMap;
    }

}
