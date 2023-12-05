<template>
    <v-avatar
        :class="classes"
        :size="size">
        <j-img :file-id="fileId" :src="src"/>
    </v-avatar>
</template>

<script>
    export default {
        name: "j-avatar",
        props: {
            size: {
                type: Number,
                default: 70
            },
            elevation: {
                type: Number,
                default: 10
            },
            fileId: {
                type: Number,
                default: null
            },
            src: {
                type: String,
                default: null
            }
        },
        computed: {
            classes() {
                return {
                    'j-avatar': true,
                    [`elevation-${this.elevation}`]: true
                }
            }
        }
    }
</script>

<style lang="scss">
.j-avatar {
    border-radius: 10px !important;
}
</style>