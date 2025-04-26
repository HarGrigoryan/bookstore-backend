package com.example.bookstore.service.parser;

import java.util.ArrayList;
import java.util.List;

public class RatingByStarsParser {

    public List<Integer> parseRatingByStars(String record) {
        List<Integer> ratings = new ArrayList<>();
        record = record.trim();
        record = record.substring(1, record.length() - 1);
        if(record.isEmpty())
            return ratings;
        String[] ratingStrings = record.trim().split(",");
        for (String ratingString : ratingStrings) {
            ratingString = ratingString.trim();
            ratings.add(Integer.parseInt(ratingString.substring(1, ratingString.length() - 1)));
        }
        return ratings;
    }

}
