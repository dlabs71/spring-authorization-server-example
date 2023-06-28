--liquibase formatted sql

--changeset daivanov:users-1
CREATE TABLE IF NOT EXISTS sso.users
(
    user_id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    email                 VARCHAR(100)                NOT NULL,
    password_hash         VARCHAR(500),
    first_name            varchar(100)                NOT NULL,
    last_name           varchar(100)                NOT NULL,
    middle_name           varchar(100),
    birthday              date,
    avatar_url            varchar(255),
    active                boolean                     not null default false,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint users_pk PRIMARY KEY (user_id)
);

COMMENT ON TABLE sso.users IS 'Пользователи';
COMMENT ON COLUMN sso.users.user_id IS 'УИ пользователя';
COMMENT ON COLUMN sso.users.email IS 'Логин пользователя';
COMMENT ON COLUMN sso.users.password_hash IS 'Хэш пароля';
COMMENT ON COLUMN sso.users.first_name IS 'Начало действия учетной записи';
COMMENT ON COLUMN sso.users.last_name IS 'Окончание действия учетной записи';
COMMENT ON COLUMN sso.users.middle_name IS 'Статус пользователя, активен или неактивен';
COMMENT ON COLUMN sso.users.birthday IS 'Пользователь, создавший текущую запись';
COMMENT ON COLUMN sso.users.avatar_url IS 'Ссылка на аватар';

COMMENT ON column sso.users.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.users.created_date IS 'Дата создания записи';
COMMENT ON column sso.users.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.users.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.users.object_version_number IS 'Номер версии записи в БД';

--changeset daivanov:users-2
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_u1 ON sso.users (email);