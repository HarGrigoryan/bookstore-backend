package com.example.bookstore.service;

import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityDeletionException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.persistance.entity.Setting;
import com.example.bookstore.persistance.repository.SettingRepository;
import com.example.bookstore.service.dto.SettingDTO;
import com.example.bookstore.service.dto.SettingRequestDTO;
import com.example.bookstore.service.mapper.SettingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;
    private final SettingMapper settingMapper;

    public SettingDTO getById(Long id) {
        return settingMapper.entityToDto(settingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Setting with id '%s' not found".formatted(id))));
    }


    public SettingDTO updateById(Long id, SettingRequestDTO settingDTO) {
        Setting setting = settingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Setting with id '%s' not found".formatted(id)));
        setting.setName(settingDTO.getName());
        return settingMapper.entityToDto(settingRepository.save(setting));
    }

    public SettingDTO createSetting( SettingRequestDTO settingDTO) {
        Setting setting = settingRepository.findByName(settingDTO.getName());
        if(setting != null) {
            throw new EntityAlreadyExistsException("Setting with name '%s' already exists".formatted(settingDTO.getName()));
        }
        setting = new Setting();
        setting.setName(settingDTO.getName());
        return settingMapper.entityToDto(settingRepository.save(setting));
    }

    public void deleteById(Long id) {
        Setting setting = settingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Setting with id '%s' not found".formatted(id)));
        try {
            settingRepository.delete(setting);
        }catch (DataIntegrityViolationException e) {
            if(e.getMostSpecificCause().getMessage().contains("violates foreign key constraint")){
                List<Long> dependentBookIds = settingRepository.dependentBookIds(id);
                throw new EntityDeletionException("Setting with id: '" + id +
                        "' could not be deleted successfully. Details: The books with the following ids %s are depended on the specified setting.".formatted(dependentBookIds));
            }
            throw new EntityDeletionException(id, e.getMessage());
        }
    }
}
