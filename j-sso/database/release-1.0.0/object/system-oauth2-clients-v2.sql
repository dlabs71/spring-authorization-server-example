--liquibase formatted sql

--changeset daivanov:system-oauth2-clients-v2-1
CREATE TABLE sso.system_oauth2_clients_v2
(
    client_id                     VARCHAR(100)                NOT NULL,
    client_secret                 VARCHAR(255),
    client_secret_expires_at      TIMESTAMP,
    client_name                   VARCHAR(200)                NOT NULL,
    client_authentication_methods VARCHAR(20)[]               NOT NULL,
    authorization_grant_types     VARCHAR(20)[]               NOT NULL,
    redirect_uris                 VARCHAR(255)[],
    scopes                        VARCHAR(100)[]              NOT NULL,
    delete_notify_uris            VARCHAR(255)[],

    created_by                    VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date                  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by               VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date             TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number         INTEGER                     NOT NULL DEFAULT 0,
    constraint system_oauth2_clients_v2_pk primary key (client_id)
);

COMMENT ON table sso.system_oauth2_clients_v2 IS 'OAuth2 клиенты системы';
COMMENT ON column sso.system_oauth2_clients_v2.client_id IS 'ID клиента';
COMMENT ON column sso.system_oauth2_clients_v2.client_secret IS 'Пароль';
COMMENT ON column sso.system_oauth2_clients_v2.client_secret_expires_at IS 'Срок действия пароля';
COMMENT ON column sso.system_oauth2_clients_v2.client_name IS 'Наименование клиента';
COMMENT ON column sso.system_oauth2_clients_v2.client_authentication_methods IS 'Доступные методы аутентификации';
COMMENT ON column sso.system_oauth2_clients_v2.authorization_grant_types IS 'Типы доступа';
COMMENT ON column sso.system_oauth2_clients_v2.redirect_uris IS 'Доступные URL-ы перенаправления';
COMMENT ON column sso.system_oauth2_clients_v2.scopes IS 'Области доступа';
COMMENT ON column sso.system_oauth2_clients_v2.delete_notify_uris IS 'Доступные URL-ы уведомления об удалении аккаунта';

COMMENT ON column sso.system_oauth2_clients_v2.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.system_oauth2_clients_v2.created_date IS 'Дата создания записи';
COMMENT ON column sso.system_oauth2_clients_v2.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.system_oauth2_clients_v2.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.system_oauth2_clients_v2.object_version_number IS 'Номер версии записи в БД';