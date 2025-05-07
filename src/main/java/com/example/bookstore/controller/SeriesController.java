package com.example.bookstore.controller;

import com.example.bookstore.service.SeriesService;
import com.example.bookstore.service.dto.SeriesCreateRequestDTO;
import com.example.bookstore.service.dto.SeriesDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping("/{id}")
    public SeriesDTO getSeriesById(@PathVariable Long id) {
        return seriesService.getSeriesById(id);
    }


    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    @PutMapping("/{id}")
    public SeriesDTO updateSeries(@Valid @RequestBody SeriesCreateRequestDTO seriesCreateRequestDTO, @PathVariable Long id) {
        return seriesService.updateSeriesById(seriesCreateRequestDTO, id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public SeriesDTO create(@RequestBody @Valid SeriesCreateRequestDTO seriesCreateRequestDTO) {
        return seriesService.create(seriesCreateRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void delete(@PathVariable Long id) {
        seriesService.delete(id);
    }

}
