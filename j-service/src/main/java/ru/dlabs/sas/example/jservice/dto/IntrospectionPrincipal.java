package ru.dlabs.sas.example.jservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntrospectionPrincipal {

    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthday;
    private String avatarUrl;
    private String username;
    private String email;
    private List<String> authorities;
}
