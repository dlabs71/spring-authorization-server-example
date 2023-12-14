<template>
    <success-form
        :show-login-btn="true"
        title="Возникла ошибка при смене пароля"
        @onLogin="onLogin"
    >
        Выполните процедуру смены пароля ещё раз
    </success-form>
</template>

<script>
    import SuccessForm from "../../components/success-form";

    export default {
        name: "error-step-change-password",
        components: {
            SuccessForm
        }
    }
</script>

<script setup>
    import {useStore} from "vuex";

    const store = useStore();

    // methods
    let onLogin = () => {
        store.dispatch('setResetStep', 0);
    };
</script>

<style></style>