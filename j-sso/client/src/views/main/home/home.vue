<template>
    <profile-form/>
    <actions-card class="mt-5"/>
</template>

<script>
    export default {
        name: "home"
    }
</script>

<script setup>
    import ProfileForm from "./forms/account-form.vue";
    import ActionsCard from "./forms/actions-card.vue";
</script>

<style lang="scss">


</style>