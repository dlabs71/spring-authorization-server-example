--liquibase formatted sql

--changeSet daivanov:authorities-data-01
INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('GET_OWN_DATA', 'Привилегия позволяет получить данные текущего пользователя', 'SSO');

INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('CHANGE_OWN_DATA', 'Привилегия позволяет изменять данные текущего пользователя', 'SSO');

INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('CHANGE_OWN_PASSWORD', 'Привилегия позволяет изменять пароль текущего пользователя', 'SSO');

INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('DELETE_OWN_ACCOUNT', 'Привилегия позволяет удалять аккаунт текущего пользователя', 'SSO');

--changeSet daivanov:authorities-data-02
INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('GET_OAUTH_CLIENT_DATA', 'Привилегия позволяет получить данные oauth2 клиентов системы', 'SSO');

INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('CHANGE_OAUTH_CLIENT_DATA', 'Привилегия позволяет изменять данные oauth2 клиентов системы', 'SSO');

INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('DELETE_OAUTH_CLIENT_DATA', 'Привилегия позволяет удалять oauth2 клиентов системы', 'SSO');

INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('GET_ADMIN_USER_DATA', 'Привилегия позволяет получить данные администраторов системы', 'SSO');

INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('ASSIGN_ADMIN_ROLE', 'Привилегия позволяет назначить/убрать роль администратора системы', 'SSO');

--changeSet daivanov:authorities-data-03
INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('GET_OWN_EVENTS', 'Привилегия позволяет просматривать историю событий пользователя', 'SSO');

--changeSet daivanov:authorities-data-04
INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('GET_OWN_TOKENS', 'Привилегия позволяет просматривать токены пользователя', 'SSO');

INSERT INTO sso.authorities(authority_code, authority_description, system_code)
VALUES ('RECALL_OWN_TOKENS', 'Привилегия позволяет отозвать токен пользователя', 'SSO');