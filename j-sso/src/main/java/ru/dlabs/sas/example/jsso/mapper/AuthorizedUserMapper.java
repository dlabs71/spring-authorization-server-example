package ru.dlabs.sas.example.jsso.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.dlabs.sas.example.jsso.dao.entity.AuthorityEntity;
import ru.dlabs.sas.example.jsso.dao.entity.RoleEntity;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.type.AuthProvider;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class AuthorizedUserMapper {

    public AuthorizedUser map(UserEntity entity, AuthProvider provider) {
        List<GrantedAuthority> authorities = getUserAuthorities(entity);
        return AuthorizedUser.builder(entity.getEmail(), entity.getPasswordHash(), authorities)
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .middleName(entity.getMiddleName())
                .birthday(entity.getBirthday())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }

    public List<GrantedAuthority> getUserAuthorities(UserEntity entity) {
        return entity.getRoles().stream()
                .filter(RoleEntity::getActive)
                .flatMap(role -> role.getAuthorities().stream())
                .filter(AuthorityEntity::getActive)
                .map(authority -> new SimpleGrantedAuthority(authority.getCode()))
                .collect(Collectors.toList());
    }
}
