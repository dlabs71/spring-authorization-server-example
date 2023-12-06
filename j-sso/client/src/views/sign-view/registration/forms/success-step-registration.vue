<template>
    <success-form
        :show-login-btn="true"
        title="Регистрация успешно завершена"
        @onLogin="onLogin"
    >
        Выполните вход, чтобы продолжить работу с системой
    </success-form>
</template>

<script>
    import SuccessForm from "../../components/success-form";

    export default {
        name: "success-step-registration",
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
        store.dispatch('setRegistrationStep', 0);
    };
</script>

<style></style>