--liquibase formatted sql

--changeSet daivanov:role-authorities-data-01
INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'USER_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'GET_OWN_DATA'));

INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'USER_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'CHANGE_OWN_DATA'));

INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'USER_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'CHANGE_OWN_PASSWORD'));

INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'USER_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'DELETE_OWN_ACCOUNT'));

--changeSet daivanov:role-authorities-data-02
INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'ADMIN_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'GET_OAUTH_CLIENT_DATA'));

INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'ADMIN_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'CHANGE_OAUTH_CLIENT_DATA'));

INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'ADMIN_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'DELETE_OAUTH_CLIENT_DATA'));

INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'ADMIN_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'GET_ADMIN_USER_DATA'));

INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'ADMIN_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'ASSIGN_ADMIN_ROLE'));

--changeSet daivanov:role-authorities-data-03
INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'USER_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'GET_OWN_EVENTS'));

--changeSet daivanov:role-authorities-data-04
INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'USER_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'GET_OWN_TOKENS'));

INSERT INTO sso.role_authorities(role_id, authority_id)
VALUES ((SELECT role_id FROM sso.roles WHERE role_code = 'USER_SSO'),
        (SELECT authority_id FROM sso.authorities WHERE authority_code = 'RECALL_OWN_TOKENS'));