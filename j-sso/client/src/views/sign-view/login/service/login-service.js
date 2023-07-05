import axios from 'axios';

export class LoginAPI {
    __LOGIN_URL = "/login";
    __OAUTH_AUTHORIZATION_URL = "/oauth2/authorization/";
    __LOCATION_HEADER = process.env.VUE_APP_SSO_LOCATION_HEADER;

    /**
     * Вход черед логин/пароль.
     * При успешной аутентификации получает в заголовках ответа специальный
     * заголовок {@see process.env.VUE_APP_SSO_LOCATION_HEADER} в котором содержится URL для дальнейшего перехода
     * @param username - логин
     * @param password - пароль
     */
    login(username, password) {
        let formData = new FormData();
        formData.append("username", username);
        formData.append("password", password);

        return axios.post(this.__LOGIN_URL, formData).then(result => {
            // проверяем есть ли спец. заголовок
            if (result.headers.has(this.__LOCATION_HEADER)) {

                // переходим на указанный в заголовке адрес
                window.location = result.headers.get(this.__LOCATION_HEADER);
            }
        });
    }

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


export default new LoginAPI();