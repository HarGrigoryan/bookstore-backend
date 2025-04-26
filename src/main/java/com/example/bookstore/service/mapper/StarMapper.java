package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Star;
import com.example.bookstore.service.dto.StarDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StarMapper implements Mapper<Star, StarDTO> {
    @Override
    public Star dtoToEntity(StarDTO dto) {
        if (dto == null) return null;
        Star star = new Star();
        star.setId(dto.getId());
        star.setStarNumber(dto.getStarNumber());
        return star;
    }

    @Override
    public StarDTO entityToDto(Star entity) {
        if (entity == null) return null;
        StarDTO starDto = new StarDTO();
        starDto.setId(entity.getId());
        starDto.setStarNumber(entity.getStarNumber());
        return starDto;
    }

    @Override
    public List<StarDTO> entityToDto(Iterable<Star> entities) {
        List<StarDTO> dtos = new ArrayList<>();
        for (Star entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

    @Override
    public List<Star> dtoToEntity(Iterable<StarDTO> dtos) {
        List<Star> stars = new ArrayList<>();
        for (StarDTO dto : dtos) {
            stars.add(dtoToEntity(dto));
        }
        return stars;
    }

}
