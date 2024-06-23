import JException from './j-exception.vue';
import exceptionStoreModule from './j-exception-store-module';

export default {
    install(Vue, options) {
        options = options || {};
        let store = options.store;
        if (!store) {
            store = Vue.$store;
        }
        if (!store) {
            throw new TypeError("store is required in options, if not define in Vue instance");
        }
        let router = options.router;
        if (!router) {
            router = Vue.$router;
        }
        if (!router) {
            throw new TypeError("router is required in options, if not define in Vue instance");
        }
        store.registerModule('exceptionStoreModule', exceptionStoreModule);
        Vue.component(JException.name, JException);
    }
}