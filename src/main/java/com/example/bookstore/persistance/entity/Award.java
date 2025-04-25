package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "award", uniqueConstraints = {@UniqueConstraint(columnNames = {"name","year"})})
@Getter
@Setter
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "award_id_sequence")
    @SequenceGenerator(name = "award_id_sequence", sequenceName = "award_id_sequence", allocationSize = 50)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "year")
    private Integer year;

    @OneToMany(mappedBy = "award")
    private List<BookAward> books = new ArrayList<>();

}
