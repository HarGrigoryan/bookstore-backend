package com.example.bookstore.service.parser;

import com.example.bookstore.enums.Format;

public class FormatParser {

    public Format parseFormat(String formatString) {
        Format[] formats = Format.values();
        for (Format format : formats) {
            if (format.name().equalsIgnoreCase(formatString)) {
                return format;
            }
            String[] formatStrings = format.toString().split("_");
            for(String formatComponent: formatStrings)
            {
                if(formatString.toLowerCase().contains(formatComponent.toLowerCase()))
                    return format;
            }
        }
        return Format.OTHER;
    }

}
