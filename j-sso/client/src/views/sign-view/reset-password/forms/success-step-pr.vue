<template>
    <success-form
        :show-login-btn="false"
        title="Аккаунт подтвержден"
        @onLogin="onLogin"
    >
        На Ваш e-mail отправлено письмо со ссылкой для создания нового пароля
    </success-form>
</template>

<script>
    import SuccessForm from "../../components/success-form";

    export default {
        name: "success-step-pr",
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