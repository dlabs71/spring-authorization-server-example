import axios from "axios";

function applyCsrfTokenFromMetaTag(requestConfig) {
    let token = searchMetaContent('_csrf');
    let tokenHeader = searchMetaContent('_csrf_header');
    if (token && tokenHeader) {
        requestConfig.headers[tokenHeader] = token;
    }
}

function applyCsrfTokenFromCookie(requestConfig) {
    let token = getCookie("XSRF-TOKEN");
    if (token) {
        requestConfig.headers["X-CSRF-TOKEN"] = token;
    }
}

function applyAxiosInterceptor(store, router) {
    axios.interceptors.request.use(config => {
        if (['DELETE', 'POST', 'PUT'].includes(config.method.toUpperCase())) {
            applyCsrfTokenFromCookie(config);
        }
        return config;
    });
    axios.interceptors.response.use(
        (response) => {
            return response;
        },
        (data) => {
            let response = data.response;
            if ("ERR_NETWORK" === data.code) {
                let payload = {
                    level: "ERROR",
                    description: "Сервис не доступен. Проверьте подключение к сети Интернет или повторите действие позднее",
                    stacktrace: []
                };
                store.dispatch('setException', payload);
                return Promise.reject(response);
            }

            let exception = response.data;
            if (exception.informative) {
                let payload = {
                    level: exception.level,
                    message: exception.message,
                }
                store.dispatch('setNotification', payload);
                return Promise.reject(response);
            }

            if (response.status === 401) {
                router.replace({name: "login"});
                return Promise.reject(response);
            }
            if (response.status === 403) {
                let payload = {
                    level: "ERROR",
                    message: "Отказано в доступе",
                }
                store.dispatch('setNotification', payload);
                return Promise.reject(response);
            }

            if (!!exception.message || !!exception.stacktrace) {
                let payload = {
                    level: "ERROR",
                    description: "Внутренняя ошибка сервиса",
                    stacktrace: []
                };
                if (!!exception.level) {
                    payload.level = exception.level;
                }
                if (!!exception.message) {
                    payload.description = exception.message;
                }
                if (!!exception.stacktrace) {
                    payload.stacktrace = exception.stacktrace;
                }

                store.dispatch('setException', payload);
            }
            return Promise.reject(response);
        });
}

function searchMetaContent(name) {
    let element = document.querySelectorAll(`meta[name='${name}']`).item(0);
    if (!element) {
        return null;
    }
    return element.getAttribute('content');
}

function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
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
