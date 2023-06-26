--liquibase formatted sql

--changeSet daivanov:system-oauth2-clients-1
create sequence sso.system_oauth2_clients_sq START 1;

--changeSet daivanov:system-oauth2-clients-2
CREATE TABLE sso.system_oauth2_clients
(
    system_client_id              BIGINT        NOT NULL DEFAULT nextval('sso.system_oauth2_clients_sq'),
    client_id                     VARCHAR(100)  NOT NULL,
    client_id_issued_at           TIMESTAMP              DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                 VARCHAR(200),
    client_secret_expires_at      TIMESTAMP,
    client_name                   VARCHAR(200)  NOT NULL,
    client_authentication_methods VARCHAR(1000) NOT NULL,
    authorization_grant_types     VARCHAR(1000) NOT NULL,
    redirect_uris                 VARCHAR(1000),
    scopes                        VARCHAR(1000) NOT NULL,
    client_settings               VARCHAR(2000),
    token_settings                VARCHAR(2000),
    constraint system_oauth2_clients_pk primary key (system_client_id)
);

COMMENT ON table sso.system_oauth2_clients IS 'OAuth2 клиенты системы';
COMMENT ON column sso.system_oauth2_clients.client_id IS 'ID клиента';
COMMENT ON column sso.system_oauth2_clients.client_id_issued_at IS 'Дата создания записи';
COMMENT ON column sso.system_oauth2_clients.client_secret IS 'Пароль';
COMMENT ON column sso.system_oauth2_clients.client_secret_expires_at IS 'Срок действия пароля';
COMMENT ON column sso.system_oauth2_clients.client_name IS 'Наименование клиента';
COMMENT ON column sso.system_oauth2_clients.client_authentication_methods IS 'Доступные методы аутентификации';
COMMENT ON column sso.system_oauth2_clients.authorization_grant_types IS 'Типы доступа';
COMMENT ON column sso.system_oauth2_clients.redirect_uris IS 'Доступные URL-ы перенаправления';
COMMENT ON column sso.system_oauth2_clients.scopes IS 'Области доступа';
COMMENT ON column sso.system_oauth2_clients.client_settings IS 'Дополнительные настройки клиента';
COMMENT ON column sso.system_oauth2_clients.token_settings IS 'Дополнительные настройки токена';

--changeset daivanov:system-oauth2-clients-3
CREATE UNIQUE INDEX idx_system_oauth2_clients_n1 ON sso.system_oauth2_clients (client_id);