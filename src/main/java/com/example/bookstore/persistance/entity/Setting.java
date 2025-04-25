package com.example.bookstore.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="setting")
@Getter
@Setter
@ToString
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "setting_id_seq")
    @SequenceGenerator(name = "setting_id_seq", sequenceName = "setting_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "setting", cascade = CascadeType.PERSIST)
    private List<BookSetting> bookSettingList = new ArrayList<>();

}
