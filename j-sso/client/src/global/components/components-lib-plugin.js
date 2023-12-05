import JTextField from './uikit/j-textfield.vue';
import JDatePicker from "./uikit/j-date-picker.vue";
import JReqDoneIndicator from "./uikit/j-req-done-indicator.vue";
import JSectionDivider from "./uikit/j-section-divider.vue";
import JAvatar from "./uikit/img/j-avatar.vue";
import JImg from "./uikit/img/j-img.vue";
import JExceptionPlugin from "./uikit/j-exception/j-exception-plugin";

import JLogo from "./specified/j-logo.vue";

export default {

    install(Vue, opts) {
        Vue.component(JTextField.name, JTextField);
        Vue.component(JDatePicker.name, JDatePicker);
        Vue.component(JReqDoneIndicator.name, JReqDoneIndicator);
        Vue.component(JSectionDivider.name, JSectionDivider);
        Vue.component(JAvatar.name, JAvatar);
        Vue.component(JImg.name, JImg);
        Vue.use(JExceptionPlugin, opts);

        Vue.component(JLogo.name, JLogo);
    }
}
