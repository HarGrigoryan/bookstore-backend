package com.example.bookstore.service;

import com.example.bookstore.enums.BookInstanceStatus;
import com.example.bookstore.exception.*;
import com.example.bookstore.persistance.entity.BookInstance;
import com.example.bookstore.persistance.entity.Coupon;
import com.example.bookstore.persistance.entity.Payment;
import com.example.bookstore.persistance.entity.Sale;
import com.example.bookstore.persistance.repository.BookInstanceRepository;
import com.example.bookstore.persistance.repository.CouponRepository;
import com.example.bookstore.persistance.repository.PaymentRepository;
import com.example.bookstore.persistance.repository.SaleRepository;
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
    private final PaymentRepository paymentRepository;
    private final BookInstanceService bookInstanceService;
    private final CouponRepository couponRepository;

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
        Long paymentId = saleCreateRequestDTO.getPaymentId();
        Sale paymentCheck = saleRepository.findByPaymentId(paymentId).orElse(null);
        if(paymentCheck != null)
            throw new ResourceAlreadyUsedException("Payment with id [%s] is already used".formatted(paymentId));
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment", paymentId));
        Integer currentRentCount = bookInstanceRepository.getCurrentRentCount(bookInstanceId);
        BigDecimal salePrice = bookInstance.getInitialPrice().setScale(5, RoundingMode.CEILING);
        if(bookInstance.getIsRentable()) {
            BigDecimal rentPrice = bookInstanceService.getRentalCost(bookInstanceId, null, null);
            salePrice = salePrice.subtract(rentPrice.multiply(BigDecimal.valueOf(currentRentCount))).setScale(5, RoundingMode.CEILING);
        }
        String couponCode = saleCreateRequestDTO.getCouponCode();
        Coupon coupon = null;
        if(couponCode != null)
        {
             coupon = validateCoupon(couponCode,  payment.getUser().getId());
            salePrice = salePrice.multiply(BigDecimal.valueOf(1).subtract(coupon.getDiscountPercentage().divide(BigDecimal.valueOf(100), 5, RoundingMode.CEILING))).setScale(5, RoundingMode.CEILING);
        }
        if(!payment.getAmount().equals(salePrice))
            throw new PaymentFailedException("Payment with id [%s] cannot be used to buy book instance [%s]".formatted(paymentId, bookInstanceId));
        Sale sale = new Sale();
        sale.setBookInstance(bookInstance);
        sale.setPayment(payment);
        sale.setCoupon(coupon);
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
        return UserDTO.toSimpleDTO(saleRepository.findUserBySaleId(id).orElseThrow(() -> new EntityNotFoundException("No user associated with sale id [%s] found".formatted(id))));
    }

    private Coupon validateCoupon(String couponCode, Long userId) {
        Coupon coupon = couponRepository.findByCode(couponCode).orElseThrow(() -> new CouponNotValidException("No coupon with the code '%s' found.".formatted(couponCode)));
        if(!coupon.getIsActive())
            throw new CouponNotValidException("Coupon with code [%s] is not active".formatted(couponCode));
        int usageCount = saleRepository.userCountByCoupon(coupon);
        if(usageCount + 1 == coupon.getUsageLimit())
            coupon.setIsActive(false);
        int usagePerUserCount = saleRepository.userCountByCouponAndUser(coupon.getId(), userId);
        if(usagePerUserCount == coupon.getUsageLimitPerUser())
            throw new CouponNotValidException
                    ("Coupon with code '%s' cannot be used by the user with id [%s] anymore.".formatted(couponCode, userId));
        return coupon;
    }

}
