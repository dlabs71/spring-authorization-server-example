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