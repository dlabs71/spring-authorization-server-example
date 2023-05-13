<template>
    <h1>
        LOGIN PAGE
    </h1>
    <button @click="login">
        LOGIN
    </button>
</template>

<script>
import LoginService from "@/services/login-service";

export default {
    name: "login",
    methods: {
        login() {
            return LoginService.login();
        }
    }
}
</script>

<style scoped>

</style>