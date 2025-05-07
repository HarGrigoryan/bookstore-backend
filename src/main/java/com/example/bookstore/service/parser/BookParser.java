package com.example.bookstore.service.parser;

import org.apache.commons.csv.CSVRecord;

import java.util.*;

import com.example.bookstore.util.*;

public class BookParser {


    public Map<String, Set<String>> parseBook(CSVRecord record) {
        Map<String, Set<String>> bookRelatedData = new HashMap<>();
        bookRelatedData.put("bookId", Utilities.singleElementSet(record.get("bookId").trim()));
        bookRelatedData.put("title", Utilities.singleElementSet(record.get("title").trim()));
        bookRelatedData.put("description", Utilities.singleElementSet(record.get("description").trim()));
        bookRelatedData.put("language", Utilities.singleElementSet(record.get("language").trim()));
        bookRelatedData.put("isbn", Utilities.singleElementSet(record.get("isbn").trim()));
        bookRelatedData.put("setting", (new SettingsParser()).parseSettingsToStrings(record.get("setting").trim()));
        bookRelatedData.put("genres", (new GenresParser()).genresToStringParser(record.get("genres").trim()));
        bookRelatedData.put("characters", (new CharactersParser()).charactersToStringParser(record.get("characters").trim()));
        bookRelatedData.put("edition", Utilities.singleElementSet(record.get("edition").trim()));
        bookRelatedData.put("pages", Utilities.singleElementSet(record.get("pages").trim()));
        bookRelatedData.put("publisherName", Utilities.singleElementSet(record.get("publisher").trim()));
        bookRelatedData.put("pageNumber", Utilities.singleElementSet(record.get("pages").trim()));
        bookRelatedData.put("price", Utilities.singleElementSet(record.get("price").trim()));
        bookRelatedData.put("publishDate", Utilities.singleElementSet(record.get("publishDate").trim()));
        bookRelatedData.put("firstPublishDate", Utilities.singleElementSet(record.get("firstPublishDate").trim()));
        bookRelatedData.put("series", Utilities.singleElementSet(record.get("series").trim()));
        bookRelatedData.put("totalStarRatings", Utilities.singleElementSet(record.get("numRatings").trim()));
        return bookRelatedData;
    }


}
