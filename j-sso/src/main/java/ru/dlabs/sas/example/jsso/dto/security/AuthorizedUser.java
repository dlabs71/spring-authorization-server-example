package ru.dlabs.sas.example.jsso.dto.security;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
public class AuthorizedUser extends User implements OAuth2User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthday;
    private UUID avatarFileId;
    private LocalDate registrationDate;
    private boolean admin;
    private boolean superuser;

    private Map<String, Object> oauthAttributes;

    public AuthorizedUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthorizedUser(
        String username,
        String password,
        boolean enabled,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public static AuthorizedUserBuilder builder(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities
    ) {
        return new AuthorizedUserBuilder(username, password, authorities);
    }

    public static AuthorizedUserBuilder builder(
        String username,
        String password,
        boolean enabled,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities
    ) {
        return new AuthorizedUserBuilder(
            username,
            password,
            enabled,
            accountNonExpired,
            credentialsNonExpired,
            accountNonLocked,
            authorities
        );
    }

    public static AuthorizedUser build(IntrospectionPrincipal principal) {
        if (principal == null) {
            return null;
        }
        List<GrantedAuthority> authorities = Collections.emptyList();
        if (principal.getAuthorities() != null) {
            authorities = principal.getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        }
        return AuthorizedUser.builder(
                principal.getEmail(),
                null,
                authorities
            )
            .id(principal.getId())
            .firstName(principal.getFirstName())
            .lastName(principal.getLastName())
            .middleName(principal.getMiddleName())
            .birthday(principal.getBirthday())
            .build();
    }

    public String getEmail() {
        return this.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauthAttributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    public static class AuthorizedUserBuilder {

        private final AuthorizedUser entity;

        AuthorizedUserBuilder(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            if (password == null) {
                password = "";
            }
            this.entity = new AuthorizedUser(username, password, authorities);
        }

        AuthorizedUserBuilder(
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities
        ) {
            this.entity = new AuthorizedUser(
                username,
                password,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities
            );
        }

        public AuthorizedUserBuilder id(UUID id) {
            this.entity.setId(id);
            return this;
        }

        public AuthorizedUserBuilder firstName(String firstName) {
            this.entity.setFirstName(firstName);
            return this;
        }

        public AuthorizedUserBuilder lastName(String lastName) {
            this.entity.setLastName(lastName);
            return this;
        }

        public AuthorizedUserBuilder middleName(String middleName) {
            this.entity.setMiddleName(middleName);
            return this;
        }

        public AuthorizedUserBuilder birthday(LocalDate birthday) {
            this.entity.setBirthday(birthday);
            return this;
        }

        public AuthorizedUserBuilder avatarFileId(UUID avatarFileId) {
            this.entity.setAvatarFileId(avatarFileId);
            return this;
        }

        public AuthorizedUserBuilder oauthAttributes(Map<String, Object> userSasInfo) {
            this.entity.setOauthAttributes(userSasInfo);
            return this;
        }

        public AuthorizedUserBuilder registrationDate(LocalDate registrationDate) {
            this.entity.setRegistrationDate(registrationDate);
            return this;
        }

        public AuthorizedUserBuilder admin(Boolean admin) {
            this.entity.setAdmin(Boolean.TRUE.equals(admin));
            return this;
        }

        public AuthorizedUserBuilder superuser(Boolean superuser) {
            this.entity.setSuperuser(Boolean.TRUE.equals(superuser));
            return this;
        }

        public AuthorizedUser build() {
            return this.entity;
        }
    }
}
