package ru.dlabs.sas.example.jsso.components;

import ru.dlabs.sas.example.jsso.dto.RegistrationDto;

public interface RegistrationStore {

    void save(RegistrationDto dto, String sessionId) throws Exception;

    RegistrationDto take(String sessionId) throws Exception;
}
