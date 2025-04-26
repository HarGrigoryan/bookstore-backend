package com.example.bookstore.service.registry;

import com.example.bookstore.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapperRegistry {
    private final Map<Class<?>, Mapper<?, ?>> mappers = new HashMap<>();

    @Autowired
    public MapperRegistry(List<Mapper<?, ?>> mapperList) {
        for (Mapper<?, ?> mapper : mapperList) {
            mappers.put(mapper.getClass(), mapper);
        }
    }

    @SuppressWarnings("unchecked")
    public <K, V> Mapper<K, V> getMapper(Class<? extends Mapper<K, V>> mapperClass) {
        return (Mapper<K, V>) mappers.get(mapperClass);
    }
}