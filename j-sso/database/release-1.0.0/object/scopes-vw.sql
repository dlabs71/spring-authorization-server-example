--liquibase formatted sql

--changeset daivanov:scopes-vw-1
create or replace view sso.scopes_vw as
(
SELECT scope_id,
       system_code || '.' || scope_code as scope_unique_code,
       scope_description,
       system_code,
       active
FROM sso.scopes
);