package com.example.bookstore.service;

import com.example.bookstore.enums.BookInstanceStatus;
import com.example.bookstore.enums.RentalStatus;
import com.example.bookstore.enums.RoleName;
import com.example.bookstore.exception.*;
import com.example.bookstore.persistance.entity.BookInstance;
import com.example.bookstore.persistance.entity.Payment;
import com.example.bookstore.persistance.entity.Rental;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.repository.BookInstanceRepository;
import com.example.bookstore.persistance.repository.PaymentRepository;
import com.example.bookstore.persistance.repository.RentalRepository;
import com.example.bookstore.persistance.repository.UserRepository;
import com.example.bookstore.service.dto.RentalCreateDTO;
import com.example.bookstore.service.dto.RentalDTO;
import com.example.bookstore.service.dto.RentalUpdateDTO;
import com.example.bookstore.service.dto.UserDTO;
import com.example.bookstore.service.email.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service("rentalService")
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final BookInstanceService bookInstanceService;
    private final EmailServiceImpl emailServiceImpl;

    @Value("${bookstore.email.new.rental.subject}")
    private String emailSubject;

    @Value("${bookstore.email.new.rental.message}")
    private String emailMessage;

    @Value("${bookstore.name}")
    private String bookStoreName;

    public void deleteById(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rental", id));
        rentalRepository.delete(rental);
    }

    public RentalDTO getById(Long id) {
        return RentalDTO.mapToDTO(rentalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rental", id)));
    }

    public RentalDTO update( RentalUpdateDTO rentalUpdateDTO, Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rental", id));
        rental.setActualReturnDate(rentalUpdateDTO.getActualReturnDate());
        Long bookInstanceId = rentalUpdateDTO.getBookInstanceId();
        rental.setBookInstance(bookInstanceRepository.findById(bookInstanceId).orElseThrow(() -> new EntityNotFoundException("BookInstance", bookInstanceId)));
        rental.setExpectedReturnDate(rentalUpdateDTO.getExpectedReturnDate());
        Long userId = rentalUpdateDTO.getUserId();
        rental.setUser(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId)));
        rental.setStatus(rentalUpdateDTO.getStatus());
        return RentalDTO.mapToDTO(rentalRepository.save(rental));
    }

    public RentalDTO create(RentalCreateDTO rentalCreateDTO) {
        Long userId = rentalCreateDTO.getUserId();
        Long bookInstanceId = rentalCreateDTO.getBookInstanceId();
        Long paymentId = rentalCreateDTO.getPaymentId();
        BookInstance bookInstance = bookInstanceRepository.findById(bookInstanceId).orElseThrow(() -> new EntityNotFoundException("BookInstance", bookInstanceId));
        if(!bookInstance.getIsRentable())
            throw new BookInstanceNotAvailable("BookInstance with id [%s] is not rentable".formatted(bookInstanceId));
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new EntityNotFoundException("Payment", paymentId));
        LocalDate expectedReturnDate = rentalCreateDTO.getExpectedReturnDate();
        BigDecimal price = bookInstanceService.getRentalCost(bookInstanceId, LocalDate.now(), expectedReturnDate);
        if(!payment.getAmount().equals(price))
            throw new PaymentFailedException("Payment with id [%s] cannot be used to rent book instance with id [%s]".formatted(paymentId, bookInstanceId));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Rental paymentCheck = rentalRepository.findRentalByPaymentId(paymentId);
        if(paymentCheck != null)
            throw new ResourceAlreadyUsedException("Payment with id [%s] is already used".formatted(paymentId));
        //checking if the book instance is currently rented
        List<Rental> rentals = rentalRepository.findByBookInstanceId(bookInstanceId);
        rentals.forEach(rental -> {
            if(rental.getActualReturnDate() == null)
                throw new EntityAlreadyExistsException("BookInstance with id [%s] is already rented.".formatted(bookInstanceId));
        });
        Rental newRental = new Rental();
        newRental.setBookInstance(bookInstance);
        newRental.setUser(user);
        newRental.setPayment(payment);
        newRental.setExpectedReturnDate(expectedReturnDate);
        newRental.setStatus(RentalStatus.IN_PROGRESS);
        rentalRepository.save(newRental);
        if(Objects.equals(bookInstanceRepository.getCurrentRentCount(bookInstanceId), bookInstance.getMaxRentCount())) {
            bookInstance.setStatus(BookInstanceStatus.TO_BE_DONATED);
            bookInstance.setIsRentable(false);
            bookInstance.setIsSellable(false);
            bookInstanceRepository.save(bookInstance);
            User manager = userRepository.findAnyByRoleName(RoleName.ROLE_MANAGER);
            if(manager != null)
                emailServiceImpl.sendEmail(manager.getEmail(), "Book To Be Donated", "Dear %s \n\nThe book instance with id %s has reached its capacity and is now marked '%s'.".formatted(manager.getFirstname(), bookInstanceId, BookInstanceStatus.TO_BE_DONATED));
        }
        emailServiceImpl.sendEmail(user.getEmail(), emailSubject, emailMessage.formatted(user.getFirstname(), bookInstance.getBook().getTitle(), newRental.getExpectedReturnDate(), bookStoreName));
        return RentalDTO.mapToDTO(newRental);
    }

    public UserDTO getUserByRentalId(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new EntityNotFoundException("Rental", rentalId));
        return UserDTO.toDTO(rental.getUser());
    }
}
