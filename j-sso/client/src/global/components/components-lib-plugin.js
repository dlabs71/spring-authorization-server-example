import JTextField from './uikit/j-textfield.vue';
import JDatePickerFlow from './uikit/j-date-picker-flow.vue';
import JException from '@/global/components/uikit/j-exception/j-exception-plugin';
import JSectionDivider from './uikit/j-section-divider.vue';
import JReqDoneIndicator from './uikit/j-req-done-indicator.vue';
import JImgChooser from './uikit/img/j-img-chooser.vue';
import JAvatar from './uikit/img/j-avatar.vue';
import JImg from './uikit/img/j-img.vue';
import JAvatarChooser from './uikit/img/j-avatar-chooser.vue';
import JMultiselect from './uikit/j-multiselect.vue';
import JDatePicker from './uikit/j-date-picker.vue';
import JConfirmationDialog from "./uikit/dialogs/j-confirmation-dialog.vue";

import JLogo from "@/global/components/app/j-logo.vue";

export default {
    install(Vue, opts) {
        Vue.component(JTextField.name, JTextField);
        Vue.component(JDatePickerFlow.name, JDatePickerFlow);
        Vue.use(JException, opts);
        Vue.component(JSectionDivider.name, JSectionDivider);
        Vue.component(JReqDoneIndicator.name, JReqDoneIndicator);
        Vue.component(JLogo.name, JLogo);
        Vue.component(JImgChooser.name, JImgChooser);
        Vue.component(JAvatar.name, JAvatar);
        Vue.component(JImg.name, JImg);
        Vue.component(JAvatarChooser.name, JAvatarChooser);
        Vue.component(JMultiselect.name, JMultiselect);
        Vue.component(JDatePicker.name, JDatePicker);
        Vue.component(JConfirmationDialog.name, JConfirmationDialog);
    }
}
