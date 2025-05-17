package com.example.bookstore.service;

import com.example.bookstore.exception.*;
import com.example.bookstore.persistance.entity.BookInstance;
import com.example.bookstore.persistance.repository.BookInstanceRepository;
import com.example.bookstore.persistance.repository.BookRepository;
import com.example.bookstore.service.criteria.BookInstanceSearchCriteria;
import com.example.bookstore.service.dto.BookInstanceDTO;
import com.example.bookstore.service.dto.BookInstanceRequestDTO;
import com.example.bookstore.service.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
            throw new EntityAlreadyExistsException("BookInstance", "book Id [%s]".formatted(bookId), "copy number [%s] ".formatted(copyNumber));
        }

        bookInstance = new BookInstance();
        bookInstance.setInstanceNumber(copyNumber);
        bookInstance.setStatus(bookInstanceRequestDTO.getStatus());
        bookInstance.setIsSellable(bookInstanceRequestDTO.getIsSellable());
        bookInstance.setIsRentable(bookInstanceRequestDTO.getIsRentable());
        bookInstance.setBook(bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book", bookId)));
        bookInstance.setInitialPrice(bookInstanceRequestDTO.getInitialPrice());
        bookInstance.setMaxRentCount(bookInstanceRequestDTO.getMaxRentCount());
        return BookInstanceDTO.toDTO(bookInstanceRepository.save(bookInstance));
    }


    public BookInstanceDTO updateById(Long id, BookInstanceRequestDTO bookInstanceRequestDTO) {
        BookInstance bookInstance = bookInstanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BookInstance", id));
        bookInstance.setInstanceNumber(bookInstanceRequestDTO.getCopyNumber());
        bookInstance.setStatus(bookInstanceRequestDTO.getStatus());
        bookInstance.setIsSellable(bookInstanceRequestDTO.getIsSellable());
        bookInstance.setIsRentable(bookInstanceRequestDTO.getIsRentable());
        bookInstance.setMaxRentCount(bookInstanceRequestDTO.getMaxRentCount());
        bookInstance.setBook(bookRepository.findById(bookInstanceRequestDTO.getBookId()).orElseThrow(() -> new EntityNotFoundException("Book", bookInstanceRequestDTO.getBookId())));
        return BookInstanceDTO.toDTO(bookInstanceRepository.save(bookInstance));
    }

    public void deleteById(Long id) {
        BookInstance bookInstance = bookInstanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Series with id '%s' found".formatted(id)));
        List<Long> dependentBookIds = bookInstanceRepository.dependentRentals(id);
        if(!dependentBookIds.isEmpty())
            throw new EntityDeletionException("BookInstance", id, dependentBookIds);
        bookInstanceRepository.delete(bookInstance);
    }

    public PageResponseDTO<BookInstanceDTO> getAll(BookInstanceSearchCriteria criteria) {
        Page<BookInstanceDTO> page = bookInstanceRepository.findAll(criteria, criteria.buildPageRequest());
        return PageResponseDTO.from(page);
    }

    public BigDecimal getRentalCost(Long id, LocalDate startDate, LocalDate endDate) {
        BookInstance bookInstance = bookInstanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BookInstance", id));
        if(!bookInstance.getIsRentable())
            throw new BookInstanceNotAvailable("Book instance with id [%s] is not rentable".formatted(id));
        BigDecimal rentalCost = bookInstance.getInitialPrice().divide(BigDecimal.valueOf(bookInstance.getMaxRentCount()), 5, RoundingMode.CEILING);
        if(startDate == null)
            startDate = LocalDate.now();
        if(endDate == null)
            endDate = startDate.plusDays(1);
        if(endDate.isBefore(startDate))
            throw new RentalRequestNotValidException("The start date cannot be later than the end date");
        return rentalCost.multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(startDate, endDate)));
    }

    public BigDecimal getSaleCost(Long id) {
        BookInstance bookInstance = bookInstanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BookInstance", id));
        if(!bookInstance.getIsSellable())
            throw new BookInstanceNotAvailable("Book instance with id [%s] is not sellable".formatted(id));
        return bookInstance.getInitialPrice().multiply(BigDecimal.valueOf(1 - bookInstanceRepository.getCurrentRentCount(id) * (100D / bookInstance.getMaxRentCount())));
    }
}
