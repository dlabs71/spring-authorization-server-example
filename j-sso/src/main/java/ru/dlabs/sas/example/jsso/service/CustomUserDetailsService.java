package ru.dlabs.sas.example.jsso.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dao.repository.UserRepository;
import ru.dlabs.sas.example.jsso.mapper.AuthorizedUserMapper;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByEmail(username);
        if (entity == null) {
            throw new UsernameNotFoundException("User with username = " + username + " not found");
        }
        return AuthorizedUserMapper.map(entity, null);
    }
}
