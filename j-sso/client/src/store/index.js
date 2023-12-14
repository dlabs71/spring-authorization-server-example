import {createStore} from 'vuex';
import VuexPersistence from 'vuex-persist'
import security from './modules/security';
import registration from '../views/sign-view/registration/store/registration';
import resetPassword from '@/views/sign-view/reset-password/store/reset-password';

const vuexLocal = new VuexPersistence({
    storage: window.localStorage,
    reducer: state => ({
        security: {
            ...state.security
        },
        registration: {
            ...state.registration
        },
        resetPassword: {
            ...state.resetPassword
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
        resetPassword: resetPassword
    },
    plugins: [vuexLocal.plugin]
});
