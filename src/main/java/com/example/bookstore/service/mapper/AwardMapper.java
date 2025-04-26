package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Award;
import com.example.bookstore.service.dto.AwardDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AwardMapper implements Mapper<Award, AwardDTO> {

    @Override
    public Award dtoToEntity(AwardDTO awardDto) {
        if (awardDto == null) {
            return null;
        }
        Award award = new Award();
        award.setId(awardDto.getId());
        award.setName(awardDto.getName());
        award.setYear(awardDto.getYear());
        return award;
    }

    @Override
    public AwardDTO entityToDto(Award award) {
        if (award == null) {
            return null;
        }
        AwardDTO awardDto = new AwardDTO();
        awardDto.setId(award.getId());
        awardDto.setName(award.getName());
        awardDto.setYear(award.getYear());
        return awardDto;
    }
    
    @Override
    public List<AwardDTO> entityToDto(Iterable<Award> awards) {
        List<AwardDTO> awardDTOS = new ArrayList<>();
        for (Award award : awards) {
            awardDTOS.add(entityToDto(award));
        }
        return awardDTOS;
    }
    
    @Override
    public List<Award> dtoToEntity(Iterable<AwardDTO> awardDtos) {
        List<Award> awards = new ArrayList<>();
        for (AwardDTO awardDto : awardDtos) {
            awards.add(dtoToEntity(awardDto));
        }
        return awards;
    }

}
