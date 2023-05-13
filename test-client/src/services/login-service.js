import axios from "axios";

const serverUrl = process.env.VUE_APP_OAUTH_URL;
axios.defaults.baseURL = serverUrl;

const clientId = process.env.VUE_APP_OAUTH_CLIENT_ID;
const authHeaderValue = process.env.VUE_APP_OAUTH_AUTH_HEADER;
const redirectUri = process.env.VUE_APP_OAUTH_REDIRECT_URI;

const ACCESS_TOKEN_KEY = "access_token";

export default {
    login() {
        let requestParams = new URLSearchParams({
            response_type: "code",
            client_id: clientId,
            redirect_uri: redirectUri,
            scope: 'read.scope write.scope'
        });
        window.location = serverUrl + "/oauth2/authorize?" + requestParams;
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