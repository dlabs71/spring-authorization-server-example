--liquibase formatted sql

--changeset daivanov:roles-1
CREATE TABLE IF NOT EXISTS sso.roles
(
    role_code             VARCHAR(50)                 NOT NULL,
    role_description      VARCHAR(500)                NOT NULL,
    active                boolean                     not null default true,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint roles_pk PRIMARY KEY (role_code)
);

COMMENT ON TABLE sso.roles IS 'Справочник ролей';
COMMENT ON COLUMN sso.roles.role_code IS 'Уникальный код роли';
COMMENT ON COLUMN sso.roles.role_description IS 'Описание роли';
COMMENT ON COLUMN sso.roles.active IS 'Флаг активности';

COMMENT ON column sso.roles.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.roles.created_date IS 'Дата создания записи';
COMMENT ON column sso.roles.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.roles.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.roles.object_version_number IS 'Номер версии записи в БД';