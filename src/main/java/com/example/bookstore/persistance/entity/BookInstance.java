package com.example.bookstore.persistance.entity;

import com.example.bookstore.enums.BookInstanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "book_instance", uniqueConstraints = {@UniqueConstraint(columnNames = {"copy_number", "book_id"})})
@Check(constraints = "status != 'SOLD' OR (is_rentable = false AND is_sellable = false)")
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

    @Column(name = "rent_count", nullable = false)
    private Integer rentCount = 0;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookInstanceStatus status = BookInstanceStatus.AVAILABLE;

    //TODO: Cascade type choice?
    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Book book;

}
