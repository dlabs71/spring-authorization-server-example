package ru.dlabs.sas.example.jsso.type;

import java.util.stream.Stream;

public enum AuthProvider {
    GOOGLE("google"),
    GITHUB("github"),
    YANDEX("yandex");

    private final String providerName;

    AuthProvider(String providerName) {
        this.providerName = providerName;
    }

    public static AuthProvider fingByName(String providerName) {
        return Stream.of(values())
                .filter(item -> item.getProviderName().equals(providerName))
                .findFirst()
                .orElse(null);
    }

    public String getProviderName() {
        return providerName;
    }
}
