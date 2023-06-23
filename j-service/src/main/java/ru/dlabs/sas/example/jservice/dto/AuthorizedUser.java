package ru.dlabs.sas.example.jservice.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizedUser implements OAuth2User {

    private UUID id;
    private String firstName;
    private String secondName;
    private String middleName;
    private LocalDate birthday;
    private String avatarUrl;
    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public static AuthorizedUser build(IntrospectionPrincipal principal) {
        if (principal == null) {
            return null;
        }
        return AuthorizedUser.builder()
                .id(principal.getId())
                .firstName(principal.getFirstName())
                .secondName(principal.getSecondName())
                .middleName(principal.getMiddleName())
                .birthday(principal.getBirthday())
                .avatarUrl(principal.getAvatarUrl())
                .username(principal.getUsername())
                .email(principal.getEmail())
                .authorities(principal.getAuthorities())
                .build();
    }


    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
