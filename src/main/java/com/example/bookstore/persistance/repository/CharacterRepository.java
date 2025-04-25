package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Override
    List<Character> findAll();
}
