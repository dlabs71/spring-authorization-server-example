import JException from './j-exception.vue';
import exceptionStoreModule from './j-exception-store-module';

export default {
    install(Vue, options) {
        options = options || {};

        // получим из параметра экземпляр vuex
        let store = options.store;

        // если нет, то попробуем получить его из экземпляра Vue.
        if (!store) {
            store = Vue.$store;
        }

        // если нет, то выбрасываем исключение
        if (!store) {
            throw new TypeError("store is required in options, if not define in Vue instance");
        }

        // регистрируем store модуль
        store.registerModule('exceptionStoreModule', exceptionStoreModule);

        // регистрируем глобальный компонент
        Vue.component(JException.name, JException);
    }
}