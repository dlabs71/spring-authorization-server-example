--liquibase formatted sql

--changeset daivanov:extensions-1
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";