package com.example.bookstore.service;

import com.example.bookstore.enums.BookInstanceStatus;
import com.example.bookstore.exception.BookInstanceNotAvailable;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.PaymentFailedException;
import com.example.bookstore.persistance.entity.BookInstance;
import com.example.bookstore.persistance.entity.Payment;
import com.example.bookstore.persistance.entity.Sale;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.repository.BookInstanceRepository;
import com.example.bookstore.persistance.repository.PaymentRepository;
import com.example.bookstore.persistance.repository.SaleRepository;
import com.example.bookstore.persistance.repository.UserRepository;
import com.example.bookstore.security.dto.SaleResponseDTO;
import com.example.bookstore.service.dto.SaleCreateRequestDTO;
import com.example.bookstore.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final BookInstanceService bookInstanceService;

    public SaleResponseDTO getById(Long id) {
        return SaleResponseDTO.toDTO(saleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sale", id)));
    }

    public SaleResponseDTO create(SaleCreateRequestDTO saleCreateRequestDTO)
    {
        Long bookInstanceId = saleCreateRequestDTO.getBookInstanceId();
        BookInstance bookInstance = bookInstanceRepository.findById(
                bookInstanceId).orElseThrow(() -> new EntityNotFoundException("BookInstance", bookInstanceId));
        if(!bookInstance.getStatus().equals(BookInstanceStatus.AVAILABLE))
            throw new BookInstanceNotAvailable("Book instance with is [%s] is not available".formatted(bookInstanceId));
        if(!bookInstance.getIsSellable())
            throw new BookInstanceNotAvailable("Book instance with is [%s] is not sellable".formatted(bookInstanceId));
        Long userId = saleCreateRequestDTO.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        Long paymentId = saleCreateRequestDTO.getPaymentId();
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment", paymentId));
        Integer currentRentCount = bookInstanceRepository.getCurrentRentCount(bookInstanceId);
        BigDecimal salePrice = bookInstance.getInitialPrice().setScale(5, RoundingMode.CEILING);
        if(bookInstance.getIsRentable()) {
            BigDecimal rentPrice = bookInstanceService.getRentingCost(bookInstanceId);
            salePrice = salePrice.subtract(rentPrice.multiply(BigDecimal.valueOf(currentRentCount))).setScale(5, RoundingMode.CEILING);
        }
        if(!payment.getAmount().equals(salePrice))
            throw new PaymentFailedException("Payment with id [%s] cannot be used to buy book instance [%s]".formatted(paymentId, bookInstanceId));
        Sale sale = new Sale();
        sale.setBookInstance(bookInstance);
        sale.setUser(user);
        sale.setPayment(payment);
        saleRepository.save(sale);
        bookInstance.setIsSellable(false);
        bookInstance.setIsRentable(false);
        bookInstance.setStatus(BookInstanceStatus.SOLD);
        bookInstanceRepository.save(bookInstance);
        return SaleResponseDTO.toDTO(saleRepository.save(sale));
    }

    // Note: this is not a 'return' logic. it is implemented to cover the case a sale was registered by mistake.
    public void deleteById(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sale", id));
        saleRepository.delete(sale);
    }

    public UserDTO getUserBySaleId(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sale", id));
        return UserDTO.toDTO(sale.getUser());
    }

}
