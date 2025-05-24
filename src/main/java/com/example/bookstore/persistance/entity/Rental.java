package com.example.bookstore.persistance.entity;

import com.example.bookstore.enums.RentalStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "rental")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "book_instance_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BookInstance bookInstance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rented_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant rentedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RentalStatus status = RentalStatus.IN_PROGRESS;

    @JoinColumn(name = "payment_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

}
