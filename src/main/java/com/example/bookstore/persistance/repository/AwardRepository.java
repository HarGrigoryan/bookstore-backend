package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

}
