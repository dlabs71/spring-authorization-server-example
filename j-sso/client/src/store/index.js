import {createStore} from 'vuex';
import VuexPersistence from 'vuex-persist'
import security from './modules/security';
import registration from '../views/sign-view/registration/store/registration';

const vuexLocal = new VuexPersistence({
    storage: window.localStorage,
    reducer: state => ({
        security: {
            ...state.security
        },
        registration: {
            ...state.registration
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
        registration: registration
    },
    plugins: [vuexLocal.plugin]
});
