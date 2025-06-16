package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Coupon;
import com.example.bookstore.persistance.entity.Sale;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.service.projection.SalePaymentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
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

    @Query( value = """
        SELECT s.id AS saleId,
            s.sold_at AS soldAt,
            p.amount AS amount,
            p.currency AS currency,
            bo.title AS title,
            string_agg(au.full_name, '\n') AS author,
            bi.id AS bookInstanceId
        FROM sale s
        LEFT JOIN payment p
          ON s.payment_id = p.id
        LEFT JOIN book_instance bi 
          ON s.book_instance_id = bi.id
        LEFT JOIN book bo 
          ON bi.book_id = bo.id
        LEFT JOIN book_author ba 
          ON ba.book_id = bo.id
        LEFT JOIN author au 
          ON ba.author_id = au.id
        WHERE s.sold_at BETWEEN :startInstant AND :endInstant
          AND p.status = 'COMPLETED'
        GROUP BY
        s.id,
        s.sold_at,
        p.amount,
        p.currency,
        bo.title,
        bi.id
        """,
            nativeQuery = true
    )
    List<SalePaymentProjection> findSalesAndPaymentsBetweenInstants(
            @Param("startInstant") Instant startInstant,
            @Param("endInstant")   Instant endInstant
    );

    @Query("""
        SELECT s
        FROM Sale s
        WHERE s.payment.id = :paymentId
        """)
    Optional<Sale> findByPaymentId(Long paymentId);

    @Query("""
        SELECT COUNT(s)
        FROM Sale s
        WHERE s.coupon = :coupon
        """)
    int userCountByCoupon(Coupon coupon);

    @Query(value = """
        SELECT COUNT(s.id)
        FROM sale s
        LEFT JOIN payment p ON s.payment_id = p.id
        WHERE s.coupon_id = :couponId
        AND  p.user_id = :userId
        """, nativeQuery = true)
    int userCountByCouponAndUser(Long couponId, Long userId);
}
