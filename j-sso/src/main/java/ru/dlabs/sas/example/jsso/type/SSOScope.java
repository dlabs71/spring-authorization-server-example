package ru.dlabs.sas.example.jsso.type;

import lombok.Getter;

/**
 * Справочник scope для системы SSO. Является отображением из таблицы sso.scopes_vw.
 * <p>
 * <div><strong>Project name:</strong> spring-authorization-server-example</div>
 * <div><strong>Creation date:</strong> 2024-05-05</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */

@Getter
public enum SSOScope {
    USER_IDENTIFICATION("SSO.USER_IDENTIFICATION"),
    USER_PROFILE_INFO("SSO.USER_PROFILE_INFO"),
    USER_AVATAR("SSO.USER_AVATAR"),
    USER_AUTHORITIES("SSO.USER_AUTHORITIES");

    private final String databaseCode;

    SSOScope(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public static SSOScope findByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
