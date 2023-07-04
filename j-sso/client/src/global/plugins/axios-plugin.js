import axios from "axios";

function applyAxiosInterceptor(store, router) {
    axios.interceptors.request.use(config => {
        return config;
    });
    axios.interceptors.response.use(function (response) {
        return response;
    }, (data) => {
        let response = data.response;
        if (response.status === 401) {
            router.replace({name: "login"});
            return Promise.reject(response);
        }
        return Promise.reject(response);
    });
}


export default {
    install(Vue, options) {
        const axiosHost = process.env.VUE_APP_SERVER === 'remote' ? process.env.VUE_APP_HOST : window.location.host;
        axios.defaults.baseURL = process.env.VUE_APP_PROTOCOL + '://' + axiosHost + process.env.VUE_APP_CONTEXT;
        axios.defaults.withCredentials = true;
        axios.defaults.hostURL = process.env.VUE_APP_PROTOCOL + '://' + axiosHost;
        applyAxiosInterceptor(options.store, options.router);
    }
}
