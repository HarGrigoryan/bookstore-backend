package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_setting", uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id","setting_id"})})
@Getter
@Setter
public class BookSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_setting_id_seq")
    @SequenceGenerator(name = "book_setting_id_seq", sequenceName = "book_setting_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="setting_id")
    private Setting setting;

}
