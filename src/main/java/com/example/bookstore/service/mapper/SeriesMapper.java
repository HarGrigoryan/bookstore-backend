package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Series;
import com.example.bookstore.service.dto.SeriesDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeriesMapper implements Mapper<Series, SeriesDTO> {
    @Override
    public Series dtoToEntity(SeriesDTO dto) {
        if(dto == null) {
            return null;
        }
        Series series = new Series();
        series.setId(dto.getId());
        series.setName(dto.getName());
        return series;
    }

    @Override
    public SeriesDTO entityToDto(Series entity) {
        if (entity == null)
            return null;
        SeriesDTO seriesDto = new SeriesDTO();
        seriesDto.setId(entity.getId());
        seriesDto.setName(entity.getName());
        return seriesDto;
    }

    @Override
    public List<SeriesDTO> entityToDto(Iterable<Series> entities) {
        List<SeriesDTO> seriesDTOList = new ArrayList<>();
        for (Series series : entities) {
            seriesDTOList.add(entityToDto(series));
        }
        return seriesDTOList;
    }

    @Override
    public List<Series> dtoToEntity(Iterable<SeriesDTO> dtos) {
        List<Series> seriesList = new ArrayList<>();
        for (SeriesDTO dto : dtos) {
            seriesList.add(dtoToEntity(dto));
        }
        return seriesList;
    }
}
