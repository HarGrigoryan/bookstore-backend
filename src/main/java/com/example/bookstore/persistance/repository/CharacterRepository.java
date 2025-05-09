package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    //TOCHECK
    @Override
    List<Character> findAll();

    @Query("""
        SELECT bc.book.id FROM BookCharacter bc WHERE bc.character.id = :id
        """)
    List<Long> dependentBookIds(Long id);

    @Query("""
        SELECT c FROM Character c WHERE c.fullName = :fullName AND (c.comment = :comment OR c.comment IS NULL AND :comment IS NULL)
    """)
    Character findByFullNameAndComment(String fullName, String comment);
}
