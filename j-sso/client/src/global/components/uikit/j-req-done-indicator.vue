<template>
    <v-icon :color="color">{{ icon }}</v-icon>
</template>

<script>
    export default {
        name: "j-req-done-indicator",
        props: {
            done: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            color() {
                return this.done ? "success" : "warning";
            },
            icon() {
                return this.done ? "done" : "warning";
            }
        }
    }
</script>

<style scoped>

</style>