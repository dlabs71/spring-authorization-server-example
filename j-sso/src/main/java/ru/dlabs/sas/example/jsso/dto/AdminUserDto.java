package ru.dlabs.sas.example.jsso.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthday;
    private LocalDate registrationDate;
    private boolean superuser;
}
