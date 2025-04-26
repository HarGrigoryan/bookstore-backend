package com.example.bookstore.service.parser;

import org.apache.commons.csv.CSVRecord;

import java.util.*;

import com.example.bookstore.util.*;

public class BookParser {


    public Map<String, Set<String>> parseBook(CSVRecord record) {
        Map<String, Set<String>> bookRelatedData = new HashMap<>();
        bookRelatedData.put("bookId", Utility.singleElementSet(record.get("bookId").trim()));
        bookRelatedData.put("title", Utility.singleElementSet(record.get("title").trim()));
        bookRelatedData.put("description", Utility.singleElementSet(record.get("description").trim()));
        bookRelatedData.put("language", Utility.singleElementSet(record.get("language").trim()));
        bookRelatedData.put("isbn", Utility.singleElementSet(record.get("isbn").trim()));
        bookRelatedData.put("setting", (new SettingsParser()).parseSettingsToStrings(record.get("setting").trim()));
        bookRelatedData.put("genres", (new GenresParser()).genresToStringParser(record.get("genres").trim()));
        bookRelatedData.put("characters", (new CharactersParser()).charactersToStringParser(record.get("characters").trim()));
        bookRelatedData.put("edition", Utility.singleElementSet(record.get("edition").trim()));
        bookRelatedData.put("pages", Utility.singleElementSet(record.get("pages").trim()));
        bookRelatedData.put("publisherName", Utility.singleElementSet(record.get("publisher").trim()));
        bookRelatedData.put("pageNumber", Utility.singleElementSet(record.get("pages").trim()));
        bookRelatedData.put("price", Utility.singleElementSet(record.get("price").trim()));
        bookRelatedData.put("publishDate", Utility.singleElementSet(record.get("publishDate").trim()));
        bookRelatedData.put("firstPublishDate", Utility.singleElementSet(record.get("firstPublishDate").trim()));
        bookRelatedData.put("series", Utility.singleElementSet(record.get("series").trim()));
        bookRelatedData.put("totalStarRatings", Utility.singleElementSet(record.get("numRatings").trim()));
        return bookRelatedData;
    }


}
