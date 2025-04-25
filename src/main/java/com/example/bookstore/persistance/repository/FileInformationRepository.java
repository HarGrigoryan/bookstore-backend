package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.FileInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInformationRepository extends JpaRepository<FileInformation, Long> {
}
