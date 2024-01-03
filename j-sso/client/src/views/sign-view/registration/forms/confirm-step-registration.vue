<template>
    <confirm-form
        ref="confirmFormRef"
        :isShowed="isShowed"
        @resend="resendCode"
        @submit="submit"
    />
</template>

<script>
    import ConfirmForm from "../../components/confirm-form";

    export default {
        name: "confirm-step-registration",
        components: {
            ConfirmForm
        }
    }
</script>

<script setup>
    import {defineEmits, ref, computed, defineExpose} from "vue";
    import {useStore} from "vuex";
    import {STEPS} from "./constants";

    const store = useStore();
    const emit = defineEmits(['next', 'resend']);

    // refs
    const confirmFormRef = ref(null);

    // computed
    let isShowed = computed(() => store.getters.getRegistrationStep === STEPS.CONFIRM);

    let submit = (code) => {
        emit('next', code);
    };

    let resetState = () => {
        if (!!confirmFormRef.value) {
            confirmFormRef.value.resetState();
        }
    };
    let resendCode = () => {
        emit('resend');
    };

    defineExpose({confirmFormRef, resetState})
</script>

<style></style>