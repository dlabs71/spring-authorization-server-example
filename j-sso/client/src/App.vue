<template>
    <v-app>
        <v-main>
            <j-exception/>
            <notifications group="main"/>
            <router-view/>
        </v-main>
    </v-app>
</template>

<script>
    export default {
        name: 'App'
    }
</script>

<style lang="scss">
    .v-main {
        .vue-notification-group {
            margin-top: 5px;
        }
    }
</style>
