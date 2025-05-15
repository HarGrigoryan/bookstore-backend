package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("""
    SELECT r FROM Rental r WHERE r.bookInstance.id = :bookInstanceId
    """)
    List<Rental> findByBookInstanceId(Long bookInstanceId);

    @Query("""
        SELECT r FROM Rental r WHERE r.payment.id = :paymentId
        """)
    Rental findRentalByPaymentId(Long paymentId);
}
