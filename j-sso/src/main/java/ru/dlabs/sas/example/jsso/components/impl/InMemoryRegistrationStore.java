package ru.dlabs.sas.example.jsso.components.impl;

import java.util.HashMap;
import java.util.Map;
import ru.dlabs.sas.example.jsso.components.RegistrationStore;
import ru.dlabs.sas.example.jsso.dto.RegistrationDto;

public class InMemoryRegistrationStore implements RegistrationStore {

    private final Map<String, RegistrationDto> store = new HashMap<>();

    @Override
    public void save(RegistrationDto dto, String sessionId) throws Exception {
        store.put(sessionId, dto);
    }

    @Override
    public RegistrationDto take(String sessionId) throws Exception {
        RegistrationDto dto = store.get(sessionId);
        store.remove(sessionId);
        return dto;
    }
}
