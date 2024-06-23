--liquibase formatted sql

--changeSet daivanov:users-data-02
INSERT INTO sso.users (email, password_hash, first_name, last_name, middle_name, birthday, active, admin)
VALUES ('admin@example.com', '$2a$10$VUqrcPxSpEhmYjIZ5zbygu3bEf1KHw8A8Vm4agZwh061SVFGr2OUG', 'Иван', 'Иванов',
        'Иванович', '1978-03-12', true, true)
ON CONFLICT(email) DO UPDATE SET admin = true;

--changeSet daivanov:users-data-03
UPDATE sso.users SET superuser = TRUE WHERE email = 'admin@example.com';

--changeSet daivanov:users-data-04
INSERT INTO sso.users (email, password_hash, first_name, last_name, middle_name, birthday, active, admin, superuser)
VALUES ('evil_mail@example.com',
        '$2a$10$dCnYBix1KP6OzN0IFJMHo.S.7iDZE8WUORjC2NDvH9dzV6eJBBE0G', 'Иван', 'Иванов', 'Иванович', '1978-03-12',
        true, false, false);