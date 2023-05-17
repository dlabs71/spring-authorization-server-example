package ru.dlabs.sas.example.jsso.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class AuthorizedUser extends User implements OAuth2User {

    private UUID id;
    private String firstName;
    private String secondName;
    private String middleName;
    private LocalDate birthday;
    private String avatarUrl;

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

    public static AuthorizedUserBuilder builder(String username, String password, Collection<? extends GrantedAuthority> authorities) {
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
        return new AuthorizedUserBuilder(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
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
            this.entity = new AuthorizedUser(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        }

        public AuthorizedUserBuilder id(UUID id) {
            this.entity.setId(id);
            return this;
        }

        public AuthorizedUserBuilder firstName(String firstName) {
            this.entity.setFirstName(firstName);
            return this;
        }

        public AuthorizedUserBuilder secondName(String secondName) {
            this.entity.setSecondName(secondName);
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

        public AuthorizedUserBuilder avatarUrl(String avatarUrl) {
            this.entity.setAvatarUrl(avatarUrl);
            return this;
        }

        public AuthorizedUserBuilder oauthAttributes(Map<String, Object> userSasInfo) {
            this.entity.setOauthAttributes(userSasInfo);
            return this;
        }

        public AuthorizedUser build() {
            return this.entity;
        }
    }
}
