import axios from 'axios';

export class LoginAPI {
    __LOGIN_URL = "/login";
    __OAUTH_AUTHORIZATION_URL = "/oauth2/authorization/";

    login(username, password) {
        let formData = new FormData();
        formData.append("username", username);
        formData.append("password", password);
        return axios.post(this.__LOGIN_URL, formData, {
            maxRedirects: 0
        }).then(result => {
            if (result.redirected) {
                window.location = result.url;
            }
        }).catch(result => {
            console.error(result);
        });
    }

    __getOAuthAuthorizationUrl(providerName) {
        return this.__OAUTH_AUTHORIZATION_URL + providerName;
    }

    loginWith(providerName) {
        window.location = this.__getOAuthAuthorizationUrl(providerName);
    }
}


export default new LoginAPI();