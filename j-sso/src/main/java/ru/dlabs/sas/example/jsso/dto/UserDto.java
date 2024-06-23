package ru.dlabs.sas.example.jsso.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserDto {

    @JsonProperty(required = true)
    private UUID id;

    @JsonProperty(required = true)
    private String email;

    @JsonProperty(required = true)
    private String firstName;

    @JsonProperty(required = true)
    private String lastName;
    private String middleName;
    private LocalDate birthday;
    private UUID avatarFileId;
    private LocalDate registrationDate;
}
