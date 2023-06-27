--liquibase formatted sql

--changeSet daivanov:role-authorities-data-01
INSERT INTO sso.role_authorities(role_code, authority_code)
VALUES ('USER_SSO', 'GET_OWN_DATA');

INSERT INTO sso.role_authorities(role_code, authority_code)
VALUES ('USER_SSO', 'CHANGE_OWN_DATA');

INSERT INTO sso.role_authorities(role_code, authority_code)
VALUES ('USER_SSO', 'CHANGE_OWN_PASSWORD');

INSERT INTO sso.role_authorities(role_code, authority_code)
VALUES ('USER_SSO', 'DELETE_OWN_ACCOUNT');