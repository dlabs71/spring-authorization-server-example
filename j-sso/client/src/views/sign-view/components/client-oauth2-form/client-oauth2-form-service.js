class ClientOauth2FormAPI {
    __OAUTH_AUTHORIZATION_URL = "/oauth2/authorization/";

    /**
     * Метод запуска процесса авторизации через Yandex, Github или Google
     * @param providerName - одно из следующих значений: google, github, yandex
     */
    loginWith(providerName) {
        window.location = this.__getOAuthAuthorizationUrl(providerName);
    }

    __getOAuthAuthorizationUrl(providerName) {
        return this.__OAUTH_AUTHORIZATION_URL + providerName;
    }
}

export default new ClientOauth2FormAPI();