import AxiosPlugin from './axios-plugin'
import vuetify from "./vuetify-plugin.js";
import veeValidate from './vee-validate';
import Notifications from '@kyvg/vue3-notification'

export default {
    install(Vue, opts) {
        Vue.use(AxiosPlugin, opts);
        Vue.use(vuetify, opts);
        Vue.use(veeValidate, opts);
        Vue.use(Notifications, opts);
    }
}
