package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Setting;
import com.example.bookstore.service.dto.SettingDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettingMapper implements Mapper<Setting, SettingDTO> {

    @Override
    public Setting dtoToEntity(SettingDTO settingDto) {
        if(settingDto==null)
            return null;
        Setting setting = new Setting();
        setting.setId(settingDto.getId());
        setting.setName(settingDto.getName());
        return setting;
    }

    @Override
    public SettingDTO entityToDto(Setting setting) {
        if(setting==null)
            return null;
        SettingDTO settingDto = new SettingDTO();
        settingDto.setId(setting.getId());
        settingDto.setName(setting.getName());
        return settingDto;
    }

    @Override
    public List<Setting> dtoToEntity(Iterable<SettingDTO> settingDtos) {
        List<Setting> settings = new ArrayList<>();
        for (SettingDTO settingDto : settingDtos) {
            Setting setting = dtoToEntity(settingDto);
            settings.add(setting);
        }
        return settings;
    }

    @Override
    public List<SettingDTO> entityToDto(Iterable<Setting> settings) {
        List<SettingDTO> settingDTOS = new ArrayList<>();
        for (Setting setting : settings) {
            SettingDTO settingDto = entityToDto(setting);
            settingDTOS.add(settingDto);
        }
        return settingDTOS;
    }
}
