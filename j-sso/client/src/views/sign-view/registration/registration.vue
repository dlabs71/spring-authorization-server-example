<template>
    <form-wrapper>
        <h2 class="mb-3">{{ header }}</h2>
        <div class="slider">
            <v-scroll-x-reverse-transition>
                <mode-step-registration v-show="showMode" @next="enterData"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <data-step-registration v-show="showData" @next="enterData"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <password-step-registration v-show="showPassword" ref="passwordStepFormRef" @next="enterData"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <confirm-step-registration v-show="showConfirm" ref="confirmStepFormRef" @next="enterData"
                                           @resend="sendRegData"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <success-step-registration v-show="showSuccess"/>
            </v-scroll-x-reverse-transition>
        </div>
        <div class="bottom-container">
            <div v-if="showBackBtn" class="link" @click="backStep">Назад</div>
            <div v-if="showLoginBtn" class="link" @click="goToLogin">На страницу входа</div>
        </div>
    </form-wrapper>
</template>

<script>
    import FormWrapper from '../components/form-wrapper';
    import DataStepRegistration from "./forms/data-step-registration";
    import PasswordStepRegistration from "./forms/password-step-registration";
    import ConfirmStepRegistration from "./forms/confirm-step-registration";
    import SuccessStepRegistration from "./forms/success-step-registration";
    import RegistrationService from "./service/registration-service";

    export default {
        name: "register-form",
        components: {
            FormWrapper,
            DataStepRegistration,
            PasswordStepRegistration,
            ConfirmStepRegistration,
            SuccessStepRegistration
        }
    }
</script>

<script setup>
    import {computed, ref, defineExpose} from "vue";
    import {RegistrationModel} from "./service/models";
    import {useStore} from "vuex";
    import {STEPS} from "./forms/constants";
    import {useRouter} from "vue-router";
    import ModeStepRegistration from "@/views/sign-view/registration/forms/mode-step-registration";

    const store = useStore();
    const router = useRouter();

    // refs
    const passwordStepFormRef = ref(null);
    const confirmStepFormRef = ref(null);
    defineExpose({passwordStepFormRef, confirmStepFormRef});

    // data
    let infoUser = new RegistrationModel();

    // computed
    let step = computed(() => store.getters.getRegistrationStep);
    let header = computed(() => {
        if (step.value === STEPS.CHOSE_MODE) {
            return "Зарегистрироваться через";
        }
        if (step.value === STEPS.DATA) {
            return "Регистрация";
        }
        if (step.value === STEPS.PASSWORD) {
            return "Придумайте пароль";
        }
        if (step.value === STEPS.CONFIRM) {
            return "Подтверждение";
        }
        if (step.value === STEPS.SUCCESS) {
            return "";
        }
    });
    let showMode = computed(() => step.value === STEPS.CHOSE_MODE);
    let showData = computed(() => step.value === STEPS.DATA);
    let showPassword = computed(() => step.value === STEPS.PASSWORD);
    let showConfirm = computed(() => step.value === STEPS.CONFIRM);
    let showSuccess = computed(() => step.value === STEPS.SUCCESS);
    let showBackBtn = computed(() => [STEPS.PASSWORD, STEPS.DATA].includes(step.value));
    let showLoginBtn = computed(() => [STEPS.DATA, STEPS.PASSWORD, STEPS.CONFIRM, STEPS.CHOSE_MODE].includes(step.value));

    // methods
    let nextStep = () => {
        store.dispatch('nextRegistrationStep');
    };
    let backStep = () => {
        store.dispatch('backRegistrationStep');
    };
    let goToLogin = () => {
        router.replace({name: "login"});
    };

    let sendRegData = () => {
        let data = store.getters.getRegistrationData;
        return RegistrationService.sendRegistrationData(RegistrationModel.build(data));
    };

    let enterData = (dataForm) => {
        if (showMode.value) {
            nextStep();
            return;
        }
        if (showData.value) {
            store.dispatch('setRegistrationData', dataForm);
            nextStep();
            return;
        }
        if (showPassword.value) {
            let data = store.getters.getRegistrationData;
            if (!data || !data.email || !data.firstName) {
                store.dispatch('setRegistrationData', null);
                backStep();
                return;
            }
            data.password = dataForm.password;
            data.repeatedPassword = dataForm.repeatedPassword;
            store.dispatch('setRegistrationData', data);

            sendRegData()
                .then(() => {
                    nextStep();
                })
                .catch(() => {
                    passwordStepFormRef.value.resetState();
                });
            return;
        }
        if (showConfirm.value) {
            RegistrationService.sendConfirm(dataForm)
                .then(() => {
                    nextStep();
                    store.dispatch('setRegistrationData', null);
                })
                .catch(() => {
                    confirmStepFormRef.value.resetState();
                });

        }
    };
</script>

<style lang="scss" scoped>
.slider {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    width: 100%;
}

.bottom-container {
    position: absolute;
    bottom: 20px;

    .link {
        align-self: center;
        margin: 5px auto 15px auto;
        cursor: pointer;
        font-size: 12px;
        opacity: 0.5;
        transition: opacity .3s ease-out;
        width: fit-content;
        font-style: italic;

        &:hover {
            opacity: 1;
        }
    }
}
</style>