package ru.dlabs.sas.example.jsso.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@Builder
public class TokenInfoDto {

    private Boolean active;
    private Object principal;
    private Collection<? extends GrantedAuthority> authorities;

}
