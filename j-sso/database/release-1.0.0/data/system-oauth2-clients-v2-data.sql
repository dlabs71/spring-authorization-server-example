--liquibase formatted sql

--changeSet daivanov:system-oauth2-clients-v2-data-01
INSERT INTO sso.system_oauth2_clients_v2(client_id, client_secret,
                                         client_secret_expires_at,
                                         client_name, client_authentication_methods,
                                         authorization_grant_types, redirect_uris,
                                         scopes, delete_notify_uris)
SELECT client_id,
       client_secret,
       client_secret_expires_at,
       client_name,
       string_to_array(client_authentication_methods, ',')::varchar[] as client_authentication_methods,
       string_to_array(authorization_grant_types, ',')::varchar[]     as authorization_grant_types,
       string_to_array(redirect_uris, ',')::varchar[]                 as redirect_uris,
       string_to_array(scopes, ',')::varchar[]                        as scopes,
       null                                                           as delete_notify_uris
FROM sso.system_oauth2_clients;

--changeSet daivanov:system-oauth2-clients-v2-data-02
UPDATE sso.system_oauth2_clients_v2
SET scopes = '{SSO.USER_PROFILE_INFO,SSO.USER_AVATAR,SSO.USER_IDENTIFICATION,SSO.USER_AUTHORITIES}'
WHERE client_id in ('test-client', 'swagger-client');

--changeSet daivanov:system-oauth2-clients-v2-data-03
UPDATE sso.system_oauth2_clients_v2
SET authorization_grant_types = '{authorization_code,refresh_token,client_credentials}'
WHERE client_id = 'test-client';

--changeSet daivanov:system-oauth2-clients-v2-data-04
-- client_secret = sso-client
INSERT INTO sso.system_oauth2_clients_v2 (client_id, client_secret, client_secret_expires_at, client_name,
                                          client_authentication_methods, authorization_grant_types, redirect_uris,
                                          scopes, delete_notify_uris, created_by, created_date, last_updated_by,
                                          last_updated_date, object_version_number)
VALUES ('sso-client', '$2a$10$T4DxYiAniqUPez5EIDp96eZpv7TC8wevcPzy7.doQ9EZjaM0/1RcS', '2072-01-01 00:00:00.000000',
        'SSO клиент', '{client_secret_basic}', '{client_credentials}',
        '{}', '{}', null, 'system', '2024-06-02 17:40:03.678306', 'system', '2024-06-02 17:40:03.678306', 0);

--changeSet daivanov:system-oauth2-clients-v2-data-05
-- client_secret = j-service-client
INSERT INTO sso.system_oauth2_clients_v2 (client_id, client_secret, client_secret_expires_at, client_name,
                                          client_authentication_methods, authorization_grant_types, redirect_uris,
                                          scopes, delete_notify_uris, created_by, created_date, last_updated_by,
                                          last_updated_date, object_version_number)
VALUES ('j-service-client', '$2a$10$VUGAIiHoa1y8fuk/gjwVr.n5Rp4oP7PELX8Egpgk5.jQGfxLGkO32',
        '2072-01-01 00:00:00.000000',
        'Клиент приложения j-service', '{client_secret_basic}', '{client_credentials}',
        '{}', '{}', null, 'system', '2024-06-02 17:40:03.678306', 'system', '2024-06-02 17:40:03.678306', 0);

--changeSet daivanov:system-oauth2-clients-v2-data-06
UPDATE sso.system_oauth2_clients_v2
SET authorization_grant_types = '{refresh_token,client_credentials,authorization_code,password}'
WHERE client_id = 'test-client';
