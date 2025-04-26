package com.example.bookstore.service.parser;

import com.example.bookstore.service.dto.SeriesDTO;
import com.example.bookstore.service.exception.UnsupportedFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeriesParser {


    public SeriesDTO parseSeries(String seriesString) {
        seriesString = seriesString.trim();
        if(seriesString.isEmpty()) {return null;}
        String seriesName = seriesString.split("#")[0];
        SeriesDTO series = new SeriesDTO();
        series.setName(seriesName.trim());
        return series;
    }

    public String parseSeriesNumber(String seriesString) throws UnsupportedFormatException {
        seriesString = seriesString.trim();
        if(seriesString.isEmpty()) {return null;}
        String[] seriesInformation = seriesString.split("#");
        if(seriesInformation.length == 1) {return null;}
        if(seriesInformation.length != 2) {
            throw new UnsupportedFormatException("Series Format " + seriesString + " is not supported");
        }
        String potentialSeriesNumber= seriesInformation[seriesInformation.length-1];
        try{
            Integer.parseInt(potentialSeriesNumber);
            return potentialSeriesNumber;
        }catch(NumberFormatException e) {
            String regex = "^[0-9]+(?:-|.)[0-9]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(potentialSeriesNumber);
            if (matcher.matches()) {
                return potentialSeriesNumber;
            } else throw new UnsupportedFormatException("Series Format " + seriesString + " is not supported");
        }
    }



}
