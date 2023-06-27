--liquibase formatted sql

--changeset daivanov:authorities-1
CREATE TABLE IF NOT EXISTS sso.authorities
(
    authority_code        VARCHAR(100)                NOT NULL,
    authority_description VARCHAR(500)                NOT NULL,
    system_code           VARCHAR(50)                 NOT NULL,
    active                boolean                     not null default true,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint authorities_pk PRIMARY KEY (authority_code)
);

COMMENT ON TABLE sso.authorities IS 'Справочник привилегий';
COMMENT ON COLUMN sso.authorities.authority_code IS 'Уникальный код привилегии';
COMMENT ON COLUMN sso.authorities.authority_description IS 'Описание привилегии';
COMMENT ON COLUMN sso.authorities.system_code IS 'Код системы, к которой принадлежит привилегия';
COMMENT ON COLUMN sso.authorities.active IS 'Флаг активности';

COMMENT ON column sso.authorities.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.authorities.created_date IS 'Дата создания записи';
COMMENT ON column sso.authorities.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.authorities.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.authorities.object_version_number IS 'Номер версии записи в БД';