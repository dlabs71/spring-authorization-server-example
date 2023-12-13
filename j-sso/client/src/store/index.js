import {createStore} from 'vuex';
import VuexPersistence from 'vuex-persist'
import security from './modules/security';
import registration from '../views/sign-view/registration/store/registration';
import forgetPassword from '../views/sign-view/forget-password/store/forget-password';

const vuexLocal = new VuexPersistence({
    storage: window.localStorage,
    reducer: state => ({
        security: {
            ...state.security
        },
        registration: {
            ...state.registration
        },
        forgetPassword: {
            ...state.forgetPassword
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
        registration: registration,
        forgetPassword: forgetPassword
    },
    plugins: [vuexLocal.plugin]
});
