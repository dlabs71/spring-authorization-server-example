package ru.dlabs.sas.example.jsso.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    @JsonProperty(required = true)
    private String email;

    @JsonProperty(required = true)
    private String firstName;

    @JsonProperty(required = true)
    private String secondName;
    private String middleName;
    private LocalDate birthday;

    @JsonProperty(required = true)
    private String password;

    public String getEmail() {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase();
    }
}
