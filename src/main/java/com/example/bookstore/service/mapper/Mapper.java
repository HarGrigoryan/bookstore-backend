package com.example.bookstore.service.mapper;

import java.util.List;

public interface Mapper<E, D> {
    E dtoToEntity(D dto);
    D entityToDto(E entity);
    List<D> entityToDto(Iterable<E> entities);
    List<E> dtoToEntity(Iterable<D> dtos);
}
