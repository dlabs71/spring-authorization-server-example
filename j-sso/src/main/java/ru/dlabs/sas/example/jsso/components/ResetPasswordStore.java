package ru.dlabs.sas.example.jsso.components;

public interface ResetPasswordStore {

    void save(StoreItem item, String sessionId) throws Exception;

    StoreItem take(String sessionId) throws Exception;

    record StoreItem(String email, String confirmCode) { }
}
