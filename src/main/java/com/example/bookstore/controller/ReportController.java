package com.example.bookstore.controller;

import com.example.bookstore.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    @PreAuthorize("hasPermission('ANY', 'GENERATE_REPORT')")
    public void getSalesReport(LocalDate startDate, LocalDate endDate, String emailTo){
        reportService.getSalesReport(startDate, endDate, emailTo);
    }

}
