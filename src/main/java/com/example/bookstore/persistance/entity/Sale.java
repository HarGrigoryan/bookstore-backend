package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity()
@Table(name = "sale")
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "book_instance_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BookInstance bookInstance;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sold_At", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant soldAt;

    @JoinColumn(name = "paymend_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

}
