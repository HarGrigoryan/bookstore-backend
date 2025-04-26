package com.example.bookstore.service.parser;

import com.example.bookstore.service.dto.PublisherDTO;


public class PublisherParser {

    public PublisherDTO parsePublisher(String publisherString) {
        if (publisherString == null || publisherString.isEmpty()) {
            return null;
        }
        PublisherDTO publisherDto = new PublisherDTO();
        publisherDto.setName(publisherString.trim());
        return publisherDto;
    }

}
