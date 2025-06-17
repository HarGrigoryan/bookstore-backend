package com.example.bookstore.service;

import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.persistance.entity.Award;
import com.example.bookstore.persistance.repository.AwardRepository;
import com.example.bookstore.service.dto.AwardDTO;
import com.example.bookstore.service.dto.AwardRequestDTO;
import com.example.bookstore.service.mapper.AwardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;

    public AwardDTO getById(Long id) {
        Award award = awardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Award", id));
        return awardMapper.entityToDto(award);
    }


    public AwardDTO updateAwardById(Long id, AwardRequestDTO requestDTO) {
        Award award = awardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Award", id));
        award.setName(requestDTO.getName());
        award.setYear(requestDTO.getYear());
        return awardMapper.entityToDto(awardRepository.save(award));
    }

    public AwardDTO createAward(AwardRequestDTO requestDTO) {
        Award award = awardRepository.findByName(requestDTO.getName()).orElse(null);
        if(award != null)
            throw new EntityAlreadyExistsException("Award", "name '%s'".formatted(requestDTO.getName()));
        award = new Award();
        award.setName(requestDTO.getName());
        award.setYear(requestDTO.getYear());
        return awardMapper.entityToDto(awardRepository.save(award));
    }

    public void deleteById(Long id) {
        Award award = awardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Award", id));
        List<Long> dependentBookIds = awardRepository.publishedBookIds(id);
        if(!dependentBookIds.isEmpty())
            throw new EntityDeletionException("Author", id, dependentBookIds);
        awardRepository.delete(award);
    }
}
