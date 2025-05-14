package com.example.bookstore.service;

import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.persistance.entity.Series;
import com.example.bookstore.persistance.repository.SeriesRepository;
import com.example.bookstore.service.dto.SeriesCreateRequestDTO;
import com.example.bookstore.service.dto.SeriesDTO;
import com.example.bookstore.service.mapper.SeriesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;

    public SeriesDTO create(SeriesCreateRequestDTO seriesCreateRequestDTO) {
        Series series = seriesRepository.findByName(seriesCreateRequestDTO.getName());
        if(series != null) {
            throw new EntityAlreadyExistsException("Series", "name '%s".formatted(seriesCreateRequestDTO.getName()));
        }
        series = new Series();
        series.setName(seriesCreateRequestDTO.getName());
        return seriesMapper.entityToDto(seriesRepository.save(series));
    }

    public void delete(Long id) {
        Series series = seriesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Series with id '%s' found".formatted(id)));
        List<Long> dependentBookIds = seriesRepository.dependentBookIds(id);
        if (!dependentBookIds.isEmpty())
            throw new EntityDeletionException("Author", id, dependentBookIds);
        seriesRepository.delete(series);
    }

    public SeriesDTO getSeriesById(Long id) {
        return seriesMapper.entityToDto(seriesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Series", id)));
    }

    public SeriesDTO updateSeriesById(SeriesCreateRequestDTO seriesCreateRequestDTO, Long id) {
        Series series = seriesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Series", id));
        series.setName(seriesCreateRequestDTO.getName());
        return seriesMapper.entityToDto(seriesRepository.save(series));
    }
}
