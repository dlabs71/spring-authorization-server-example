<template>
    <h1>HOME PAGE</h1>
    <p aria-multiline="true" aria-rowcount="20">
        {{ tokenInfoString }}
    </p>
</template>

<script>

import LoginService from "@/services/login-service";

export default {
    name: "home",
    data: () => {
        return {
            tokenInfo: {}
        }
    },
    methods: {
        getCurrentPrincipal() {
            LoginService.getTokenInfo()
                .then(result => {
                    console.log("Result getting token info: ", result);
                    if (!result.data.active) {
                        this.$router.replace({name: "login"});
                        return;
                    }
                    this.tokenInfo = result.data;
                })
                .catch((err) => {
                    console.log("Error getting token info: ", err);
                    this.$router.replace({name: "login"});
                })
        }
    },
    computed: {
        tokenInfoString() {
            if (!this.tokenInfo) {
                return null;
            }
            return JSON.stringify(this.tokenInfo, null, 8);
        }
    },
    mounted() {
        this.getCurrentPrincipal();
    }
}
</script>

<style scoped>
    p {
        white-space: pre-wrap;
        text-align: left;
        margin-left: 20px;
        font-size: 1.5em;
    }
</style>