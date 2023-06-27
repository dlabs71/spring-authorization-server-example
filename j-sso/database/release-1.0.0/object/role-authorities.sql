--liquibase formatted sql

--changeSet daivanov:role-authorities-1
create sequence sso.role_authorities_sq START 1;

--changeset daivanov:role-authorities-2
CREATE TABLE IF NOT EXISTS sso.role_authorities
(
    role_authority_id BIGINT                      NOT NULL DEFAULT nextval('sso.role_authorities_sq'),
    role_code         VARCHAR(50)                 NOT NULL,
    authority_code    VARCHAR(50)                 NOT NULL,

    created_by        VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    constraint role_authorities_pk PRIMARY KEY (role_authority_id)
);

COMMENT ON TABLE sso.role_authorities IS 'Маппинг ролей и привилегий';
COMMENT ON COLUMN sso.role_authorities.role_authority_id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN sso.role_authorities.role_code IS 'Уникальный код привилегии';
COMMENT ON COLUMN sso.role_authorities.authority_code IS 'Уникальный код привилегии';

COMMENT ON column sso.role_authorities.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.role_authorities.created_date IS 'Дата создания записи';

--changeset daivanov:role-authorities-3
CREATE UNIQUE INDEX idx_role_authorities_u1 ON sso.role_authorities (role_code, authority_code);