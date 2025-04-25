package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.BookSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSettingRepository extends JpaRepository<BookSetting, Long> {
}
