package com.example.bookstore.service.registry;

import com.example.bookstore.persistance.entity.*;
import com.example.bookstore.persistance.entity.Character;
import com.example.bookstore.persistance.repository.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Getter
public class EntityMapRegistry {

    private final ConcurrentMap<Class<?>, ConcurrentMap<String,?>> entityMap;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final CharacterRepository characterRepository;
    private final PublisherRepository publisherRepository;
    private final LanguageRepository languageRepository;
    private final SettingRepository settingRepository;
    private final SeriesRepository seriesRepository;
    private final AwardRepository awardRepository;

    @Autowired
    public EntityMapRegistry(GenreRepository genreRepository, AuthorRepository authorRepository, CharacterRepository characterRepository, PublisherRepository publisherRepository, LanguageRepository languageRepository, SettingRepository settingRepository, SeriesRepository seriesRepository, AwardRepository awardRepository) {
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.characterRepository = characterRepository;
        this.publisherRepository = publisherRepository;
        this.languageRepository = languageRepository;
        this.settingRepository = settingRepository;
        this.seriesRepository = seriesRepository;
        this.awardRepository = awardRepository;
        entityMap = new ConcurrentHashMap<>();
        entityMap.put(Author.class, new ConcurrentHashMap<>());
        entityMap.put(Character.class, new ConcurrentHashMap<>());
        entityMap.put(Award.class, new ConcurrentHashMap<>());
        entityMap.put(Publisher.class, new ConcurrentHashMap<>());
        entityMap.put(Setting.class, new ConcurrentHashMap<>());
        entityMap.put(Genre.class, new ConcurrentHashMap<>());
        entityMap.put(Language.class, new ConcurrentHashMap<>());
        entityMap.put(Series.class, new ConcurrentHashMap<>());
        entityMap.put(Star.class, new ConcurrentHashMap<>());
        entityMap.put(Book.class, new ConcurrentHashMap<>());

    }

    public ConcurrentMap<String, ?> entityMap(Class<?> entityClass) {
        return entityMap.get(entityClass);
    }

    @SuppressWarnings("unchecked")
    public void saveAll(){
        genreRepository.saveAll(((ConcurrentMap<String, Genre>) entityMap.get(Genre.class)).values());
        authorRepository.saveAll(((ConcurrentMap<String, Author>) entityMap.get(Author.class)).values());
        characterRepository.saveAll(((ConcurrentMap<String, Character>) entityMap.get(Character.class)).values());
        publisherRepository.saveAll(((ConcurrentMap<String, Publisher>) entityMap.get(Publisher.class)).values());
        languageRepository.saveAll(((ConcurrentMap<String, Language>) entityMap.get(Language.class)).values());
        settingRepository.saveAll(((ConcurrentMap<String, Setting>) entityMap.get(Setting.class)).values());
        seriesRepository.saveAll(((ConcurrentMap<String, Series>) entityMap.get(Series.class)).values());
        awardRepository.saveAll(((ConcurrentMap<String, Award>) entityMap.get(Award.class)).values());
    }

}
