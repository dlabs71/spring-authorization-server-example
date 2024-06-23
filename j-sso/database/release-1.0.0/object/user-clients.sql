--liquibase formatted sql

--changeset daivanov:user-client-1
CREATE TABLE IF NOT EXISTS sso.user_clients
(
    user_client_id        UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    user_id               UUID                        NOT NULL,
    client_id             VARCHAR(100)                not null,
    deleted               boolean                     not null default false,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint user_client_pk PRIMARY KEY (user_client_id)
);

COMMENT ON TABLE sso.user_clients IS 'Связь пользователя и клиента системы для которого проходил аутентификацию';
COMMENT ON COLUMN sso.user_clients.user_client_id IS 'УИ записи';
COMMENT ON COLUMN sso.user_clients.user_id IS 'УИ пользователя';
COMMENT ON COLUMN sso.user_clients.client_id IS 'УИ клиента';
COMMENT ON COLUMN sso.user_clients.deleted IS 'Запись помечена на удаление';

COMMENT ON column sso.user_clients.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.user_clients.created_date IS 'Дата создания записи';
COMMENT ON column sso.user_clients.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.user_clients.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.user_clients.object_version_number IS 'Номер версии записи в БД';

--changeset daivanov:user-client-2
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_u1 ON sso.user_clients (user_id, client_id);