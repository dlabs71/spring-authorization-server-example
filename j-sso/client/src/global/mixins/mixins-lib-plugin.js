import DateHelperMixin from './date-helper-mixin'
import NotificationMixin from './notification-mixin';

export default {
    install(Vue, opt) {
        Vue.mixin(DateHelperMixin);
        Vue.mixin(NotificationMixin);
    }
}
