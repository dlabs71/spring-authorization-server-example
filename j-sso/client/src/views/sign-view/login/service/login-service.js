import axios from 'axios';
import store from '@/store/index.js';

export class LoginAPI {
    static __LOGIN_URL = "/login";
    static __USER_DATA_URL = '/security-session/user';
    static __LOCATION_HEADER = process.env.VUE_APP_SSO_LOCATION_HEADER;

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

        return axios.post(LoginAPI.__LOGIN_URL, formData)
            .then(result => {
                if (result.headers.has(LoginAPI.__LOCATION_HEADER)) {
                    this.getCurrentUser().then(() => {
                        window.location = result.headers.get(LoginAPI.__LOCATION_HEADER);
                    });
                }
            });
    }

    /**
     * Получаем данные об авторизованном пользователе
     * @returns {Promise<axios.AxiosResponse<any>>} данные ответа сервера
     */
    getCurrentUser() {
        return axios.get(LoginAPI.__USER_DATA_URL).then(result => {
            console.log(result.data);
            store.dispatch('setAuthUser', result.data);
            return result.data;
        });
    }
}


export default new LoginAPI();