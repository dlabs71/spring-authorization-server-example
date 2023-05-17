package ru.dlabs.sas.example.jsso.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    private UUID id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String secondName;
    private String middleName;
    private LocalDate birthday;
    private String avatarUrl;
    private Boolean active;
}
