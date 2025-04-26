package com.example.bookstore.service.parser;

import com.example.bookstore.service.dto.AwardDTO;

import java.util.*;

public class AwardsParser {

    public Set<AwardDTO> parseAwards(String awardsString) {
        Set<AwardDTO> awardDTOList = new HashSet<>();
        awardsString = awardsString.trim();
        String[] parts = awardsString.substring(1, awardsString.length()-1).split("(?<=['\"]),");
        for (String part : parts) {
            String trimmedPart =part.trim();
            if(trimmedPart.length()<2) {continue;}
            String awardChunk = trimmedPart.substring(1, trimmedPart.length()-1).trim();
            String[] awards = awardChunk.split(",(?![^(]*\\))");
            for(String award : awards)
            {
                String awardName;
                Integer year = null;
                if(award.contains(")"))
                {
                    String[] awardArray = award.split(" ");
                    String lastElement = awardArray[awardArray.length-1];
                    String potentialAwardYear=null;
                    try {
                        potentialAwardYear= lastElement.substring(1, lastElement.length() - 1);
                    }catch (StringIndexOutOfBoundsException e){
                        System.out.println(award);
                        System.out.println(Arrays.toString(awardArray));
                        System.exit(3);
                    }
                    try
                    {
                        year = Integer.parseInt(potentialAwardYear);
                        awardName = String.join(" ",
                                Arrays.copyOf(awardArray, awardArray.length - 1)).trim();

                    }
                    catch (NumberFormatException e)
                    {
                        awardName=award.trim();
                    }
                }
                else {
                    awardName = award;
                }
                AwardDTO finalAwardDTO = new AwardDTO();
                finalAwardDTO.setYear(year);
                finalAwardDTO.setName(awardName);
                awardDTOList.add(finalAwardDTO);
            }
        }

        return awardDTOList;
    }

    public Set<String> parseAwardsString(String awardsString) {
        Set<String> awardNames = new HashSet<>();
        Set<AwardDTO> awardDTOS = parseAwards(awardsString.trim());
        for (AwardDTO awardDto : awardDTOS) {
            awardNames.add(awardDto.getName() + awardDto.getYear());
        }
        return awardNames;
    }

}
