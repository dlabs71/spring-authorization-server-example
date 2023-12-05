import NotificationMixin from './notification-mixin';

export default {
    install(Vue, opt) {
        Vue.mixin(NotificationMixin);
    }
}
