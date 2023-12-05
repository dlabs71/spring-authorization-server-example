import {createStore} from 'vuex';
import VuexPersistence from 'vuex-persist'
import security from './modules/security';

const vuexLocal = new VuexPersistence({
    storage: window.localStorage,
    reducer: state => ({
        security: {
            ...state.security
        }
    })
});

export default createStore({
    state: {},
    getters: {},
    mutations: {},
    actions: {},
    modules: {
        security: security,
    },
    plugins: [vuexLocal.plugin]
});
