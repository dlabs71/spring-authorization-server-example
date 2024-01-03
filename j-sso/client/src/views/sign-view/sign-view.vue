<template>
    <div class="login-page">
        <router-view/>
    </div>
</template>

<script>

    export default {
        name: "sign-view"
    }
</script>

<style lang="scss">
.login-page {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100vh;
    width: 100vw;
    background: linear-gradient(to bottom right, #5909ef 0%, #ac27f2 100%);
}
</style>