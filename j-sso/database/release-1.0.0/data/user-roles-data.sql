--liquibase formatted sql

--changeSet daivanov:users-role-data-01
INSERT INTO sso.user_roles(user_id, role_code)
SELECT user_id, 'USER_SSO'
FROM sso.users
ON CONFLICT DO NOTHING;