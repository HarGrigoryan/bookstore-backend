package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Sale;
import com.example.bookstore.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {


    @Query("""
    SELECT u
    FROM Sale s
    INNER JOIN Payment p ON s.payment = p
    INNER JOIN User u ON p.user = u
    WHERE s.id = :saleId
    """)
    Optional<User> findUserBySaleId(Long saleId);

}
