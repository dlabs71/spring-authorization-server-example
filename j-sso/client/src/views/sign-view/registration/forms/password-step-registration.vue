<template>
    <password-form
        ref="passwordFormRef"
        @submit="submit"
    ></password-form>
</template>

<script>
    import PasswordFormCom from "../../components/password-form";

    export default {
        name: "password-step-registration",
        components: {
            "password-form": PasswordFormCom
        }
    }
</script>

<script setup>
    import {computed, defineEmits, ref, watch, defineExpose} from 'vue';
    import {STEPS} from "./constants";
    import {useStore} from "vuex";

    const store = useStore();

    // refs
    const passwordFormRef = ref(null);

    // data
    const emit = defineEmits(['next']);

    // computed
    let isShowed = computed(() => store.getters.getRegistrationStep === STEPS.PASSWORD);
    let storedRegistrationData = computed(() => store.getters.getRegistrationData);

    // watch
    watch(() => isShowed.value, (value) => {
        if (value) {
            if (!!storedRegistrationData.value && !!passwordFormRef.value) {
                passwordFormRef.value.setData(
                    storedRegistrationData.value.password,
                    storedRegistrationData.value.repeatedPassword
                );
            }
        }
    });

    // methods
    let submit = (dataForm) => {
        emit('next', dataForm);
    };
    let resetState = () => {
        if (!!passwordFormRef.value) {
            passwordFormRef.value.resetState();
        }
    };

    defineExpose({passwordFormRef, resetState})
</script>

<style></style>