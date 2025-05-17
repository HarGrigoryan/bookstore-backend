package com.example.bookstore.persistance.entity;

import com.example.bookstore.enums.BookInstanceStatus;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book_instance", uniqueConstraints = {@UniqueConstraint(columnNames = {"instance_number", "book_id"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instance_number")
    private Integer instanceNumber;

    @Column(name = "is_rentable", nullable = false)
    private Boolean isRentable = false;

    @Column(name = "is_sellable", nullable = false)
    private Boolean isSellable = true;

    @Column(name = "max_rent_count", nullable = false)
    private Integer maxRentCount = 0;

    @Column(name="price", nullable = false, precision = 10, scale = 2)
    private BigDecimal initialPrice;

    @Column(name = "last_updated_at", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookInstanceStatus status = BookInstanceStatus.AVAILABLE;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Book book;

    @OneToMany(mappedBy = "bookInstance", cascade = {
        CascadeType.MERGE, CascadeType.REMOVE})
    private List<Rental> rentals;


}
