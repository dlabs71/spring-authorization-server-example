--liquibase formatted sql

--changeSet daivanov:1
create table sso.file_storage
(
    file_id               uuid                        not null default uuid_generate_v4(),
    store_type            varchar(50)                 not null,
    filename              varchar(255)                not null,
    file_size             bigint                      not null,
    content_type          varchar(100)                not null,
    bucket                varchar(50)                 not null,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint file_storage_pk PRIMARY KEY (file_id)
);

COMMENT ON table sso.file_storage IS 'Хранилище файлов';

COMMENT ON column sso.file_storage.file_id IS 'Уникальный ID записи';
COMMENT ON column sso.file_storage.store_type IS 'Тип загруженного файла';
COMMENT ON column sso.file_storage.filename IS 'Наименование файла';
COMMENT ON column sso.file_storage.file_size IS 'Размер файла (байт)';
COMMENT ON column sso.file_storage.content_type IS 'MIME Type';
COMMENT ON column sso.file_storage.bucket IS 'Директория хранения';

COMMENT ON column sso.roles.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.roles.created_date IS 'Дата создания записи';
COMMENT ON column sso.roles.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.roles.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.roles.object_version_number IS 'Номер версии записи в БД';
