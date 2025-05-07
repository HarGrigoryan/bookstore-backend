package com.example.bookstore.service;

import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.persistance.entity.BookInstance;
import com.example.bookstore.persistance.repository.BookInstanceRepository;
import com.example.bookstore.persistance.repository.BookRepository;
import com.example.bookstore.service.dto.BookInstanceDTO;
import com.example.bookstore.service.dto.BookInstanceRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookInstanceService {

    private final BookInstanceRepository bookInstanceRepository;
    private final BookRepository bookRepository;


    public BookInstanceDTO getById(Long id) {
        return BookInstanceDTO.toDTO(bookInstanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BookInstance", id)));
    }

    public BookInstanceDTO create( BookInstanceRequestDTO bookInstanceRequestDTO) {
        Long bookId = bookInstanceRequestDTO.getBookId();
        Integer copyNumber = bookInstanceRequestDTO.getCopyNumber();
        BookInstance bookInstance = bookInstanceRepository.findByCopyNumberAndBook(copyNumber, bookId);
        if (bookInstance != null) {
            throw new EntityAlreadyExistsException("BookInstance with book id '%s' and copy number '%s' already exists".formatted(bookId, copyNumber));
        }

        bookInstance = new BookInstance();
        bookInstance.setInstanceNumber(copyNumber);
        bookInstance.setStatus(bookInstanceRequestDTO.getStatus());
        bookInstance.setIsSellable(bookInstanceRequestDTO.getIsSellable());
        bookInstance.setIsRentable(bookInstanceRequestDTO.getIsRentable());
        bookInstance.setBook(bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book", bookId)));
        return BookInstanceDTO.toDTO(bookInstanceRepository.save(bookInstance));
    }


    public BookInstanceDTO updateById(Long id, BookInstanceRequestDTO bookInstanceRequestDTO) {
        BookInstance bookInstance = bookInstanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BookInstance", id));
        bookInstance.setInstanceNumber(bookInstanceRequestDTO.getCopyNumber());
        bookInstance.setStatus(bookInstanceRequestDTO.getStatus());
        bookInstance.setIsSellable(bookInstanceRequestDTO.getIsSellable());
        bookInstance.setIsRentable(bookInstanceRequestDTO.getIsRentable());
        bookInstance.setRentCount(bookInstanceRequestDTO.getRentCount());
        bookInstance.setBook(bookRepository.findById(bookInstanceRequestDTO.getBookId()).orElseThrow(() -> new EntityNotFoundException("Book", bookInstanceRequestDTO.getBookId())));
        return BookInstanceDTO.toDTO(bookInstanceRepository.save(bookInstance));
    }

    public void deleteById(Long id) {
        BookInstance bookInstance = bookInstanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Series with id '%s' found".formatted(id)));
        try {
            bookInstanceRepository.delete(bookInstance);
        }catch (DataIntegrityViolationException e) {
            if(e.getMostSpecificCause().getMessage().contains("violates foreign key constraint")){
                List<Long> dependentBookIds = bookInstanceRepository.dependentRentals(id);
                throw new EntityDeletionException("BookInstance with id: '" + id +
                        "' could not be deleted successfully. Details: The rentals with the following ids %s are depended on the specified BookInstance.".formatted(dependentBookIds));
            }
            throw new EntityDeletionException(id, e.getMessage());
        }
    }

/*    public PageResponseDTO<BookInstanceDTO> getAll(BookInstanceSearchCriteria criteria) {
        Page<BookInstanceDTO> page = bookInstanceRepository.findAll(criteria, criteria.buildPageRequest());
        return PageResponseDTO.from(page);
    }*/
}
