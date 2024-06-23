--liquibase formatted sql

--changeSet daivanov:scopes-data-01
INSERT INTO sso.scopes(scope_code, scope_description, system_code)
VALUES ('USER_IDENTIFICATION', 'Идентификационная информация о пользователе (email, ID)', 'SSO');

INSERT INTO sso.scopes(scope_code, scope_description, system_code)
VALUES ('USER_PROFILE_INFO', 'Общая информация о пользователе (ФИО, дата рождения)', 'SSO');

INSERT INTO sso.scopes(scope_code, scope_description, system_code)
VALUES ('USER_AVATAR', 'Аватар пользователя', 'SSO');

INSERT INTO sso.scopes(scope_code, scope_description, system_code)
VALUES ('USER_AUTHORITIES', 'Привилегии пользователя', 'SSO');

