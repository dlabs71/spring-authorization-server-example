--liquibase formatted sql

--changeSet daivanov:users-data-01
UPDATE sso.users SET email = lower(trim(email));