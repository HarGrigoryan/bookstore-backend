package com.example.bookstore.service;

import com.example.bookstore.enums.PaymentCurrency;
import com.example.bookstore.enums.PaymentStatus;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.PaymentFailedException;
import com.example.bookstore.persistance.entity.Payment;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.repository.PaymentRepository;
import com.example.bookstore.persistance.repository.UserRepository;
import com.example.bookstore.service.dto.PaymentDTO;
import com.example.bookstore.service.dto.PaymentRequestDTO;
import com.example.bookstore.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    /**
     * NOTE: This is a simplified mock implementation for demonstration purposes only.
     * In a production environment:
     * - Payment processing would integrate with a secure payment gateway (e.g., Stripe, PayPal)
     * - Transaction references would be generated by the payment processor
     * - Sensitive payment information would NEVER be handled directly by our service
     * The current implementation auto-approves all payments with a placeholder reference
     * to avoid third-party service dependencies in this demo application.
     *
     * @see PaymentRequestDTO, the paymentInformation - Would normally contain encrypted payment details
     */
    public PaymentDTO pay(PaymentRequestDTO paymentRequestDTO)
    {
        Long userId = paymentRequestDTO.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        Payment payment = new Payment();
        payment.setCurrency(paymentRequestDTO.getCurrency());
        payment.setUser(user);
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        String transactionReference = "PlaceHolder" + payment.getId();//since the reference is unique

        /*try{
            //process payment using paymentInformation and get the transactionReference
            transactionReference = "actual-reference";
        }
        catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentFailedException("Payment failed. Details: " + e.getMessage());
        }*/
        payment.setTransactionReference(transactionReference);
        payment.setStatus(PaymentStatus.COMPLETED);
        return PaymentDTO.toDTO(paymentRepository.save(payment));
    }

    public UserDTO getUserByPaymentId(Long id)
    {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Payment", id));
        User user = payment.getUser();
        return UserDTO.toDTO(user);
    }

    public PaymentDTO getById(Long id)
    {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Payment", id));
        return PaymentDTO.toDTO(payment);
    }

}
