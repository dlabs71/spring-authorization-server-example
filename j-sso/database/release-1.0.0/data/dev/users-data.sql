--liquibase formatted sql

--changeSet daivanov:users-data-01
INSERT INTO sso.users (email, password_hash, first_name, last_name, middle_name, birthday, avatar_url, active)
VALUES ('admin@example.com', '$2a$10$VUqrcPxSpEhmYjIZ5zbygu3bEf1KHw8A8Vm4agZwh061SVFGr2OUG', 'Иван', 'Иванов', 'Иванович', '1978-03-12', null, true);