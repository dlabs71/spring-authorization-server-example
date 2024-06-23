--liquibase formatted sql

--changeset daivanov:user-events-1
CREATE TABLE IF NOT EXISTS sso.user_events
(
    event_id      UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    event_type    VARCHAR(100)                NOT NULL,
    user_agent    VARCHAR(500)                NOT NULL,
    ip_address    varchar(50),
    client_id     varchar(50),
    agent_browser varchar(50),
    agent_device  varchar(50),
    agent_os      varchar(50),

    created_by    VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    constraint user_events_pk PRIMARY KEY (event_id)
);

COMMENT ON TABLE sso.user_events IS 'События пользователей';
COMMENT ON COLUMN sso.user_events.event_id IS 'УИ пользователя';
COMMENT ON COLUMN sso.user_events.event_type IS 'Логин пользователя';
COMMENT ON COLUMN sso.user_events.user_agent IS 'Значение заголовка запроса User-Agent';
COMMENT ON COLUMN sso.user_events.ip_address IS 'IP адрес пользователя';
COMMENT ON COLUMN sso.user_events.client_id IS 'Уникальный идентификатор клиента';
COMMENT ON COLUMN sso.user_events.agent_browser IS 'Браузер пользователя';
COMMENT ON COLUMN sso.user_events.agent_device IS 'Информация об устройстве пользователя';
COMMENT ON COLUMN sso.user_events.agent_os IS 'Информация об OS пользователя';

COMMENT ON column sso.users.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.users.created_date IS 'Дата создания записи';

--changeset daivanov:user-events-2
CREATE INDEX idx_user_events_n1 ON sso.user_events (date(created_date));

--changeset daivanov:user-events-3
CREATE INDEX idx_user_events_n2 ON sso.user_events (created_by);