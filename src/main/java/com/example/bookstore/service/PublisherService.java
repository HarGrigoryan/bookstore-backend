package com.example.bookstore.service;

import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.persistance.entity.Publisher;
import com.example.bookstore.persistance.repository.PublisherRepository;
import com.example.bookstore.service.dto.BookSearchResponseDTO;
import com.example.bookstore.service.dto.PublisherDTO;
import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.service.mapper.PublisherMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PublisherService {
    PublisherRepository publisherRepository;
    PublisherMapper publisherMapper;

    public List<BookSearchResponseDTO> getPublishedBooks(Long id) {
        Publisher publisher = publisherRepository.findById(id).orElse(null);
        if (publisher == null)
            throw new EntityNotFoundException("Publisher", id);
        return BookSearchResponseDTO.mapToDTO(publisher.getBooks());
    }


    public PublisherDTO getPublisherById(Long id) {

        Publisher publisher = publisherRepository.findById(id).orElse(null);
        if (publisher == null)
            throw new EntityNotFoundException("Publisher", id);
        return publisherMapper.entityToDto(publisher);

    }

    public PublisherDTO createPublisher(PublisherDTO publisherDto) {
        String name = publisherDto.getName();
        if(publisherRepository.existsByName(name))
            throw new EntityAlreadyExistsException("Publisher", "name '%s'".formatted(name));
        Publisher publisher = new Publisher();
        publisher.setName(name);
        return publisherMapper.entityToDto(publisherRepository.save(publisher));
    }

    public PublisherDTO updatePublisher(Long id, PublisherDTO publisherDto) {
        Publisher publisher = publisherRepository.findById(id).orElse(null);
        if (publisher == null)
            throw new EntityNotFoundException("Publisher", id);
        publisher.setName(publisherDto.getName());
        return publisherMapper.entityToDto(publisherRepository.save(publisher));
    }

    public void deletePublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Publisher", id));
        List<Long> dependentBookIds = publisherRepository.publishedBookIds(id);
        if (!dependentBookIds.isEmpty())
            throw new EntityDeletionException("Publisher", id, dependentBookIds);
        publisherRepository.delete(publisher);
    }
}
