import axios from "axios";

const serverUrl = process.env.VUE_APP_OAUTH_URL;
axios.defaults.baseURL = serverUrl;

const clientId = process.env.VUE_APP_OAUTH_CLIENT_ID;
const clientSecret = process.env.VUE_APP_OAUTH_CLIENT_SECRET;
const authHeaderValue = process.env.VUE_APP_OAUTH_AUTH_HEADER;
const redirectUri = process.env.VUE_APP_OAUTH_REDIRECT_URI;

const ACCESS_TOKEN_KEY = "access_token";
const REFRESH_TOKEN_KEY = "refresh_token";

export default {
    login() {
        let requestParams = new URLSearchParams({
            response_type: "code",
            client_id: clientId,
            redirect_uri: redirectUri,
            scope: 'SSO.USER_IDENTIFICATION SSO.USER_PROFILE_INFO SSO.USER_AVATAR SSO.USER_AUTHORITIES'
        });
        window.location = serverUrl + "/oauth2/authorize?" + requestParams;
    },

    loginClient() {
        let payload = new FormData()
        payload.append('grant_type', 'client_credentials');
        payload.append('client_id', clientId);
        payload.append('client_secret', clientSecret);
        payload.append('scope', 'SSO.USER_IDENTIFICATION SSO.USER_PROFILE_INFO SSO.USER_AVATAR SSO.USER_AUTHORITIES');

        return axios.post('/oauth2/token', payload, {
                headers: {
                    'Content-type': 'application/url-form-encoded',
                    'Authorization': authHeaderValue
                }
            }
        ).then(response => {
            console.log("Result getting tokens (client credentials): " + response.data)
            window.sessionStorage.setItem(ACCESS_TOKEN_KEY, response.data[ACCESS_TOKEN_KEY]);
            window.sessionStorage.setItem(REFRESH_TOKEN_KEY, response.data[REFRESH_TOKEN_KEY]);
        });
    },

    loginPassword() {
        let payload = new FormData()
        payload.append('grant_type', 'password');
        payload.append('username', "admin@example.com");
        payload.append('password', "admin@example.com");
        payload.append('scope', 'SSO.USER_IDENTIFICATION SSO.USER_PROFILE_INFO SSO.USER_AVATAR SSO.USER_AUTHORITIES');

        return axios.post('/oauth2/token', payload, {
                headers: {
                    'Content-type': 'application/url-form-encoded',
                    'Authorization': authHeaderValue
                }
            }
        ).then(response => {
            console.log("Result getting tokens (client credentials): " + response.data)
            window.sessionStorage.setItem(ACCESS_TOKEN_KEY, response.data[ACCESS_TOKEN_KEY]);
            window.sessionStorage.setItem(REFRESH_TOKEN_KEY, response.data[REFRESH_TOKEN_KEY]);
        });
    },

    refreshToken() {
        let payload = new FormData()
        payload.append('grant_type', 'refresh_token')
        payload.append('refresh_token', window.sessionStorage.getItem(REFRESH_TOKEN_KEY))

        return axios.post('/oauth2/token', payload, {
                headers: {
                    'Content-type': 'application/url-form-encoded',
                    'Authorization': authHeaderValue
                }
            }
        ).then(response => {
            console.log("Result refresh tokens: " + response.data)
            window.sessionStorage.setItem(ACCESS_TOKEN_KEY, response.data[ACCESS_TOKEN_KEY]);
        })
    },

    getTokens(code) {
        let payload = new FormData()
        payload.append('grant_type', 'authorization_code')
        payload.append('code', code)
        payload.append('redirect_uri', redirectUri)
        payload.append('client_id', clientId)

        return axios.post('/oauth2/token', payload, {
                headers: {
                    'Content-type': 'application/url-form-encoded',
                    'Authorization': authHeaderValue
                }
            }
        ).then(response => {
            console.log("Result getting tokens: " + response.data)
            window.sessionStorage.setItem(ACCESS_TOKEN_KEY, response.data[ACCESS_TOKEN_KEY]);
            window.sessionStorage.setItem(REFRESH_TOKEN_KEY, response.data[REFRESH_TOKEN_KEY]);
        })
    },

    getTokenInfo() {
        let payload = new FormData();
        payload.append('token', window.sessionStorage.getItem(ACCESS_TOKEN_KEY));

        return axios.post('/oauth2/token-info', payload, {
            headers: {
                'Authorization': authHeaderValue
            }
        });
    }
}