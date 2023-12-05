<template>
    <div class="client-oauth2-form">
        <v-btn class="enter-btn" icon variant="text" @click="()=>loginWith('yandex')">
            <img :src="yandexIcon"/>
        </v-btn>
        <v-btn class="enter-btn" icon variant="text" @click="()=>loginWith('google')">
            <img :src="googleIcon"/>
        </v-btn>
        <v-btn class="enter-btn" icon variant="text" @click="()=>loginWith('github')">
            <img :src="githubIcon"/>
        </v-btn>
    </div>
</template>

<script>
    export default {
        name: "client-oauth2-form"
    }
</script>

<script setup>
    import ClientOauth2FormAPI from "./client-oauth2-form-service";

    const googleIcon = require("@/assets/img/google.svg");
    const githubIcon = require("@/assets/img/github.svg");
    const yandexIcon = require("@/assets/img/yandex.svg");

    let loginWith = (providerName) => {
        ClientOauth2FormAPI.loginWith(providerName);
    }
</script>

<style lang="scss">
.client-oauth2-form {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    .enter-btn {
        margin: 5px;
        text-transform: none !important;
        border-radius: 10px;

        img {
            width: 25px;
            height: 25px;
        }
    }
}
</style>