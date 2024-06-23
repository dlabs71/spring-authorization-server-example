--liquibase formatted sql

--changeSet daivanov:roles-data-01
INSERT INTO sso.roles(role_code, role_description)
VALUES ('USER_SSO', 'Роль обычного пользователя DL-SSO');

--changeSet daivanov:roles-data-02
UPDATE sso.roles
SET role_description = 'Роль обычного пользователя J-SSO',
    system_code      = 'SSO'
WHERE system_code IS NULL
  AND role_code = 'USER_SSO';

--changeSet daivanov:roles-data-03
INSERT INTO sso.roles(role_code, role_description, system_code)
VALUES ('ADMIN_SSO', 'Роль администратора J-SSO', 'SSO');