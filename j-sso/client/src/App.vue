<template>
    <v-app>
        <v-main>
            <notifications group="main"/>
            <router-view/>
        </v-main>
    </v-app>
</template>

<script>

export default {
    name: 'App',

    data: () => ({
        //
    }),
    mounted() {
        //
    },
}
</script>
