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
    name: 'App',

    data: () => ({
        //
    }),
    mounted() {
        //
    },
}
</script>
