package com.example.bookstore.service.parser;

import com.example.bookstore.exception.UnsupportedFormatException;

import java.time.LocalDate;
import java.time.Month;

public class DateParser {

    private final static String[] monthAcronyms = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public LocalDate parseDate(String dateString) {
        if(dateString ==null ||  dateString.length() > 19)
            throw new UnsupportedFormatException("Invalid date format: " + dateString);
        int length = dateString.length();
        dateString = dateString.trim();
        if(dateString.equalsIgnoreCase("published"))
            return null;
        String[] dateParts = dateString.split(" ");
        if(dateParts.length == 2)
        {
            try{
                return LocalDate.of(Integer.parseInt(dateParts[1]), determineMonth(dateParts[0]), 1 );
            }catch (Exception e) {
                throw new UnsupportedFormatException("Invalid date format: " + dateString);
            }
        }
        if(length==4) {
            try {
                int year = Integer.parseInt(dateString);
                return LocalDate.ofYearDay(year, 1);
            } catch (NumberFormatException e) {
                throw new UnsupportedFormatException("Date format: \"" + dateString + "\" is not supported");
            }
        }
        if(dateString.contains("-"))
        {
            String[] dateComponents = dateString.split("-");
            if(dateComponents.length == 2)
            {
                for(int i=0; i<monthAcronyms.length; i++){

                    try {
                        if(dateComponents[1].equals(monthAcronyms[i]))
                            return LocalDate.of(0, i + 1, Integer.parseInt(dateComponents[0]));
                        if(dateComponents[0].equals(monthAcronyms[i]))
                            return LocalDate.of(Integer.parseInt(dateComponents[1]), i + 1,1);
                    } catch (Exception e) {
                        throw new UnsupportedFormatException("Date format: \"" + dateString + "\" is not supported. \nError message: " + e.getMessage());
                    }

                }
            }
            else
                throw new UnsupportedFormatException("Date format: \"" + dateString + "\" is not supported");
        }
        int day;
        int month;
        int year;
        LocalDate date;
        String[] dateComponents = dateString.split("/");
        if(dateComponents.length == 3){
            try{
                month = Integer.parseInt(dateComponents[0].trim());
                day = Integer.parseInt(dateComponents[1].trim());
                year = Integer.parseInt(dateComponents[2].trim());
                return LocalDate.of(year, month, day);
            }
            catch (NumberFormatException e){
                throw new UnsupportedFormatException("Invalid date format. Potential date: " + dateString + " Error message: " + e.getMessage());
            }
        }else{
            String potentialMonth = dateParts[0].trim();
            month = determineMonth(potentialMonth);
            if(month == 0)
                throw new UnsupportedFormatException("Invalid month. Potential month: " + potentialMonth);
            String potentialDay = null;
            String potentialYear = null;
            try {
                potentialDay = dateParts[1].trim();
                potentialDay = potentialDay.substring(0, potentialDay.length() - 2);
                day = Integer.parseInt(potentialDay);

            }
            catch (NumberFormatException e1)
            {
                throw new UnsupportedFormatException("Invalid day. Potential day: " + potentialDay);
            }
            try {
                potentialYear = dateParts[2].trim();
                year = Integer.parseInt(potentialYear);
                date = LocalDate.of(year, month, day);
            }
            catch (NumberFormatException e2)
            {
                throw new UnsupportedFormatException("Invalid year. Potential year: " + potentialYear);
            }
            return date;
        }
    }

    private int determineMonth(String monthString) {
        Month[] months = Month.values();
        int month = 0;
        for(Month m : months)
        {
            if(monthString.toUpperCase().equals(m.toString()))
                month = m.ordinal() + 1;
        }
        return month;
    }

}
