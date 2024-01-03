package ru.dlabs.sas.example.jsso.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewPasswordDto {

    private String confirmCode;
    private String passwordHash;
}
