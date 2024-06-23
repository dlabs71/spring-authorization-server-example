--liquibase formatted sql

--changeset daivanov:scopes-1
CREATE TABLE IF NOT EXISTS sso.scopes
(
    scope_id              UUID                        NOT NULL default uuid_generate_v4(),
    scope_code            VARCHAR(100)                NOT NULL,
    scope_description     VARCHAR(500)                NOT NULL,
    system_code           VARCHAR(50)                 NOT NULL,
    active                boolean                     not null default true,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint scopes_pk PRIMARY KEY (scope_id)
);

COMMENT ON TABLE sso.scopes IS 'Справочник привилегий';
COMMENT ON COLUMN sso.scopes.scope_id IS 'Уникальный идентификатор scope';
COMMENT ON COLUMN sso.scopes.scope_code IS 'Код scope';
COMMENT ON COLUMN sso.scopes.scope_description IS 'Описание scope';
COMMENT ON COLUMN sso.scopes.system_code IS 'Код системы, к которой принадлежит scope';
COMMENT ON COLUMN sso.scopes.active IS 'Флаг активности';

COMMENT ON column sso.scopes.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.scopes.created_date IS 'Дата создания записи';
COMMENT ON column sso.scopes.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.scopes.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.scopes.object_version_number IS 'Номер версии записи в БД';

--changeset daivanov:scopes-2
CREATE UNIQUE INDEX idx_scopes_u1 ON sso.scopes (scope_code, system_code);