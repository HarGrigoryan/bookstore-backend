package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Publisher;
import com.example.bookstore.service.dto.PublisherDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublisherMapper implements Mapper<Publisher, PublisherDTO> {
    @Override
    public Publisher dtoToEntity(PublisherDTO dto) {
        if (dto == null) return null;
        Publisher publisher = new Publisher();
        publisher.setId(dto.getId());
        publisher.setName(dto.getName());
        return publisher;
    }

    @Override
    public PublisherDTO entityToDto(Publisher entity) {
        if (entity == null) return null;
        PublisherDTO publisherDto = new PublisherDTO();
        publisherDto.setId(entity.getId());
        publisherDto.setName(entity.getName());
        return publisherDto;
    }

    @Override
    public List<PublisherDTO> entityToDto(Iterable<Publisher> entities) {
        List<PublisherDTO> publisherDTOS = new ArrayList<>();
        for (Publisher publisher : entities) {
            publisherDTOS.add(entityToDto(publisher));
        }
        return publisherDTOS;
    }

    @Override
    public List<Publisher> dtoToEntity(Iterable<PublisherDTO> dtos) {
        List<Publisher> publishers = new ArrayList<>();
        for (PublisherDTO dto : dtos) {
            publishers.add(dtoToEntity(dto));
        }
        return publishers;
    }
}
