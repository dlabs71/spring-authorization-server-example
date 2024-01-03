import JTextField from './uikit/j-textfield.vue';
import JDatePicker from "./uikit/j-date-picker.vue";
import JReqDoneIndicator from "./uikit/j-req-done-indicator.vue";
import JSectionDivider from "./uikit/j-section-divider.vue";
import JExceptionPlugin from "./uikit/j-exception/j-exception-plugin";

import JLogo from "@/global/components/app/j-logo.vue";

export default {

    install(Vue, opts) {
        Vue.component(JTextField.name, JTextField);
        Vue.component(JDatePicker.name, JDatePicker);
        Vue.component(JReqDoneIndicator.name, JReqDoneIndicator);
        Vue.component(JSectionDivider.name, JSectionDivider);
        Vue.use(JExceptionPlugin, opts);

        Vue.component(JLogo.name, JLogo);
    }
}
