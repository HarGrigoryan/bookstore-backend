package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("""
        SELECT c FROM Coupon c WHERE c.code = :code
    """)
    Optional<Coupon> findByCode(String code);
}
