<template>
    <form-wrapper>
        <h2 class="mb-3">{{ header }}</h2>
        <div class="slider">
            <v-scroll-x-reverse-transition>
                <init-form-pr v-show="showInit" ref="initStepForm" @next="enterData"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <confirm-form-pr v-show="showConfirm" ref="confirmStepForm" @next="enterData"
                                 @resend="initResetPassword"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <success-form-pr v-show="showSuccess"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <password-form-pr v-show="showPassword" @next="enterData"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <success-change-password v-show="showSuccessChange"/>
            </v-scroll-x-reverse-transition>
            <v-scroll-x-reverse-transition>
                <error-step-change-password v-show="showErrorChange"/>
            </v-scroll-x-reverse-transition>
        </div>
        <div class="bottom-container">
            <div class="link" @click="goToLogin">На страницу входа</div>
        </div>
    </form-wrapper>
</template>

<script>
    import FormWrapper from '../components/form-wrapper';
    import {Form} from 'vee-validate';
    import InitFormPr from "./forms/init-form-pr";
    import ResetPasswordService from './service/reset-password-service';
    import ConfirmFormPr from "./forms/confirm-step-pr";
    import SuccessFormPr from "./forms/success-step-pr";
    import PasswordFormPr from "./forms/password-step-pr";
    import SuccessChangePassword from './forms/success-step-change-password';
    import ErrorStepChangePassword from "./forms/error-step-change-password";

    export default {
        name: "reset-password",
        components: {
            FormWrapper,
            Form,
            InitFormPr,
            ConfirmFormPr,
            SuccessFormPr,
            PasswordFormPr,
            SuccessChangePassword,
            ErrorStepChangePassword
        }
    }
</script>

<script setup>
    import {computed, defineExpose, onMounted, ref} from "vue";
    import {STEPS} from "./forms/constants";
    import {useRouter} from "vue-router";
    import {useStore} from "vuex";

    const store = useStore();
    const router = useRouter();

    // refs
    const confirmStepForm = ref(null);
    const initStepForm = ref(null);
    defineExpose({confirmStepForm, initStepForm});

    // computed
    let step = computed(() => store.getters.getResetPasswordStep);
    let header = computed(() => {
        if (step.value === STEPS.INIT) {
            return "Сброс пароля";
        }
        if (step.value === STEPS.CONFIRM) {
            return "Подтверждение e-mail";
        }
        if (step.value === STEPS.SUCCESS) {
            return "";
        }
        if (step.value === STEPS.PASSWORD) {
            return "Придумайте новый пароль";
        }
    });
    let showInit = computed(() => step.value === STEPS.INIT);
    let showConfirm = computed(() => step.value === STEPS.CONFIRM);
    let showSuccess = computed(() => step.value === STEPS.SUCCESS);
    let showPassword = computed(() => step.value === STEPS.PASSWORD);
    let showSuccessChange = computed(() => step.value === STEPS.SUCCESS_CHANGE);
    let showErrorChange = computed(() => step.value === STEPS.ERROR_CHANGE);

    // methods
    let nextStep = () => {
        store.dispatch('nextResetStep');
    };
    let setStep = (step) => {
        store.dispatch('setResetStep', step);
    };
    let goToLogin = () => {
        router.replace({name: "login"});
    };
    let initResetPassword = (email) => {
        return ResetPasswordService.initReset(email);
    };

    let enterData = (dataForm) => {
        if (showInit.value) {
            store.dispatch('setResetData', dataForm);
            initResetPassword(dataForm.email).then(() => {
                nextStep();
            }).catch(() => {
                initStepForm.value.resetState();
            });
            return;
        }
        if (showConfirm.value) {
            ResetPasswordService.confirmReset(dataForm.confirmCode).then(() => {
                nextStep();
            }).catch(() => {
                confirmStepForm.value.resetState();
            });
            return;
        }
        if (showPassword.value) {
            let storedData = store.getters.getResetPasswordData;
            if (!storedData) {
                setStep(STEPS.ERROR_CHANGE);
                return;
            }
            let resetSessionId = storedData.resetSessionId;
            ResetPasswordService.setNewPassword(dataForm.password, resetSessionId).then(() => {
                setStep(STEPS.SUCCESS_CHANGE);
                store.dispatch('setResetData', null);
            }).catch(() => {
                confirmStepForm.value.resetState();
                setStep(STEPS.ERROR_CHANGE);
            });
        }
    };

    onMounted(() => {
        // пытаемся достать параметр из строки запроса по имени resetSessionId
        const urlParams = new URLSearchParams(window.location.search);
        const resetSessionId = urlParams.get('resetSessionId');

        // если мы смогли получить параметр resetSessionId
        if (!!resetSessionId) {

            // сразу переходим к шагу смены пароля
            setStep(STEPS.PASSWORD);
            store.dispatch('setResetData', {
                resetSessionId: resetSessionId
            });
            router.replace({name: "reset-password"})
        }
    });

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