<template>
    <success-form
        title="Пароль успешно изменен"
        :show-login-btn="true"
        @onLogin="onLogin"
    >
        Выполните вход используя новый пароль
    </success-form>
</template>

<script>
import SuccessForm from "../../components/success-form";

export default {
    name: "success-step-change-password",
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