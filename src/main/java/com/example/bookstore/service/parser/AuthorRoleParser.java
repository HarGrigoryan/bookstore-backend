package com.example.bookstore.service.parser;

import com.example.bookstore.enums.AuthorRole;

public class AuthorRoleParser {

    public AuthorRole parseAuthorRole(String authorRoleString)
    {
        if(authorRoleString == null || authorRoleString.isEmpty())
            return AuthorRole.AUTHOR;
        AuthorRole[] authorRoles = AuthorRole.values();
        for (AuthorRole authorRole : authorRoles)
        {
            String[] roles = authorRole.toString().split("_");
            if(authorRoleString.toLowerCase().contains(roles[0].toLowerCase()))
                return authorRole;
        }
        return AuthorRole.OTHER;
    }

}
