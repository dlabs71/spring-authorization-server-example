package ru.dlabs.sas.example.jsso.service.impl;

import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dlabs.sas.example.jsso.dao.entity.RoleEntity;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dao.repository.RoleRepository;
import ru.dlabs.sas.example.jsso.dao.repository.UserRepository;
import ru.dlabs.sas.example.jsso.dto.AdminUserDto;
import ru.dlabs.sas.example.jsso.dto.PageableResponseDto;
import ru.dlabs.sas.example.jsso.exception.InformationException;
import ru.dlabs.sas.example.jsso.exception.ServiceException;
import ru.dlabs.sas.example.jsso.mapper.UserDtoMapper;
import ru.dlabs.sas.example.jsso.service.AdminUserService;
import ru.dlabs.sas.example.jsso.service.UserService;
import ru.dlabs.sas.example.jsso.utils.HttpUtils;

/**
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAdminUserService implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public PageableResponseDto<AdminUserDto> searchUsers(int page, int pageSize, String email) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "creationDate"));
        Specification<UserEntity> specification = this.getSearchSpecification(email);
        Page<UserEntity> entitiesPage = userRepository.findAll(specification, pageRequest);
        List<AdminUserDto> dtoList = entitiesPage.get().map(UserDtoMapper::mapAdmin).toList();

        return PageableResponseDto.build(
            dtoList,
            page < entitiesPage.getTotalPages(),
            entitiesPage.getTotalElements()
        );
    }

    private Specification<UserEntity> getSearchSpecification(String email) {
        return (root, query, builder) -> {
            Predicate predicate = builder.isTrue(root.get("admin"));

            if (StringUtils.isNotEmpty(email)) {
                predicate = builder.and(builder.like(
                    builder.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"
                ));
            }
            return predicate;
        };
    }

    @Override
    @Transactional
    public void assignAdmin(String email) {
        UserEntity entity = userRepository.findByEmail(email);
        if (entity == null) {
            throw InformationException.builder("$user.not.found").build();
        }
        if (entity.getAdmin()) {
            throw InformationException.builder("$user.already.admin").build();
        }

        RoleEntity adminRole = roleRepository.getAdminRole();
        entity.getRoles().add(adminRole);
        entity.setAdmin(true);
        userRepository.save(entity);
    }

    @Override
    @Transactional
    public void dismissAdmin(UUID userId) {
        UserEntity entity = userRepository.getReferenceById(userId);
        if (entity.getSuperuser()) {
            throw InformationException.builder("$forbid.dismiss.superuser").build();
        }
        entity.setAdmin(false);
        entity.setRoles(
            entity.getRoles().stream()
                .filter(item -> !item.getCode().equals("ADMIN_SSO"))
                .collect(Collectors.toList())
        );
        userRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getAvatar(UUID userId) {
        try {
            UserService.UserAvatar userAvatar = userService.getUserAvatar(userId);
            return HttpUtils.appendFileToResponse(
                userAvatar.storeDto().getId().toString(),
                userAvatar.storeDto().getContentType(),
                userAvatar.avatar()
            );
        } catch (ServiceException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
