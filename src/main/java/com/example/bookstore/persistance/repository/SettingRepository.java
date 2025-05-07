package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

    @Query("SELECT s FROM Setting s WHERE s.name = :name")
    Setting findByName(String name);

    @Query("""
        SELECT b.book.id FROM Setting s left JOIN BookSetting b ON b.setting = s WHERE s.id = :id
        """)
    List<Long> dependentBookIds(Long id);
}
