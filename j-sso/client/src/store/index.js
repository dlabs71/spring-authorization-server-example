import {createStore} from 'vuex';
import VuexPersistence from 'vuex-persist'
import security from './modules/security';
import registration from '../views/sign-view/registration/store/registration';
import resetPassword from '@/views/sign-view/reset-password/store/reset-password';
import reference from './modules/reference';

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
        },
        reference: {
            ...state.reference
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
        resetPassword: resetPassword,
        reference: reference
    },
    plugins: [vuexLocal.plugin]
});
