package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Rental;
import com.example.bookstore.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query("""
        SELECT u
        FROM Rental r
        INNER JOIN Payment p On r.payment = p
        INNER JOIN User u ON p.user = u
        WHERE r.id = :rentalId
    """)
    Optional<User> findUserByRentalId(Long rentalId);
}
