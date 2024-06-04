package ru.dlabs.sas.example.jsso.dto.security;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import ru.dlabs.sas.example.jsso.type.AuthProvider;

@Getter
@Builder
@AllArgsConstructor
public class AuthorizedUserDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthday;
    private String username;
    private String email;
    private List<String> authorities;
    private LocalDate registrationDate;
    private AuthProvider authProvider;
    private boolean admin;
    private boolean superuser;

    public static AuthorizedUserDto build(AuthorizedUser authorizedUser) {
        if (authorizedUser == null) {
            return null;
        }

        List<String> authorities = Collections.emptyList();
        if (authorizedUser.getAuthorities() != null) {
            authorities = authorizedUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        }
        return AuthorizedUserDto.builder()
            .id(authorizedUser.getId())
            .firstName(authorizedUser.getFirstName())
            .lastName(authorizedUser.getLastName())
            .middleName(authorizedUser.getMiddleName())
            .birthday(authorizedUser.getBirthday())
            .username(authorizedUser.getUsername())
            .email(authorizedUser.getEmail())
            .authorities(authorities)
            .admin(authorizedUser.isAdmin())
            .superuser(authorizedUser.isSuperuser())
            .registrationDate(authorizedUser.getRegistrationDate())
            .build();
    }
}
