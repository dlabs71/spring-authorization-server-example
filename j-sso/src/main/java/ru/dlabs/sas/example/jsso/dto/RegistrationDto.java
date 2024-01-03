package ru.dlabs.sas.example.jsso.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
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
}
