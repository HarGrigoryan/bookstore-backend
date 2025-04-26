package com.example.bookstore.service.parser;

import com.example.bookstore.service.dto.SettingDTO;

import java.util.HashSet;
import java.util.Set;

public class SettingsParser {

    public Set<SettingDTO> parseSettings(String settings) {
        Set<SettingDTO> settingList = new HashSet<>();
        Set<String> settingStrings = parseSettingsToStrings(settings);
        for (String record : settingStrings) {
            SettingDTO settingDto = new SettingDTO();
            settingDto.setName(record.trim());
            settingList.add(settingDto);
        }
        return settingList;
    }

    public Set<String> parseSettingsToStrings(String settings) {
        Set<String> settingList = new HashSet<>();
        if(settings==null)
            return settingList;
        settings = settings.trim();
        if(settings.length()<2)
            return settingList;
        settings = settings.substring(1,settings.length()-1).trim();
        String[] settingsArray = settings.split("(?<=['\"]),");
        for (String setting : settingsArray) {
            setting = setting.trim();
            if(setting.length() > 2)
                settingList.add(setting.substring(1,setting.length()-1));
        }
        return settingList;
    }

}
