package com.example.bookstore.controller;

import com.example.bookstore.service.SeriesService;
import com.example.bookstore.service.dto.SeriesCreateRequestDTO;
import com.example.bookstore.service.dto.SeriesDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    @GetMapping
    public List<SeriesDTO> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @PermitAll
    @GetMapping("/{id}")
    public SeriesDTO getSeriesById(@PathVariable Long id) {
        return seriesService.getSeriesById(id);
    }


    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{id}")
    public SeriesDTO updateSeries(@Valid @RequestBody SeriesCreateRequestDTO seriesCreateRequestDTO, @PathVariable Long id) {
        return seriesService.updateSeriesById(seriesCreateRequestDTO, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public SeriesDTO create(@RequestBody @Valid SeriesCreateRequestDTO seriesCreateRequestDTO) {
        return seriesService.create(seriesCreateRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_INFORMATION')")
    public void delete(@PathVariable Long id) {
        seriesService.delete(id);
    }

}
