import SettingsMixin from './settings-mixin';
import RouterMixin from './router-mixin';
import NotificationMixin from './notification-mixin';

export default {
    install(Vue, opt) {
        Vue.mixin(SettingsMixin);
        Vue.mixin(RouterMixin);
        Vue.mixin(NotificationMixin);
    }
}
