import axios from 'axios';
import store from '@/store/index.js';

export class LoginAPI {
    __LOGIN_URL = "/login";
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

        return axios.post(this.__LOGIN_URL, formData)
            .then(result => {
                if (result.headers.has(this.__LOCATION_HEADER)) {
                    this.getCurrentUser().then(() => {
                        window.location = result.headers.get(this.__LOCATION_HEADER);
                    });
                }
            });
    }

    /**
     * Получаем данные об авторизованном пользователе
     * @returns {Promise<axios.AxiosResponse<any>>} данные ответа сервера
     */
    getCurrentUser() {
        return axios.get('/security-session/user').then(result => {
            console.log(result.data);
            store.dispatch('setAuthUser', result.data);
            return result.data;
        });
    }
}


export default new LoginAPI();