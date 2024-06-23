<template>
    <v-avatar
        :class="classes"
        :size="size">
        <j-img
            v-if="showImg"
            :src="src"
            :url="url"
            @errorOnLoad="errorOnLoad"
            :size="size"
        />
        <h1 v-else>{{ text }}</h1>
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
            src: {
                type: String,
                default: null
            },
            userName: {
                type: String,
                default: null
            },
            squad: {
                type: Boolean,
                default: false
            },
            url: {
                type: String,
                default: null
            }
        },
        data() {
            return {
                errorOnLoadImg: false
            }
        },
        methods: {
            errorOnLoad() {
                this.errorOnLoadImg = true;
            }
        },
        computed: {
            text() {
                if (!this.userName) {
                    return "";
                }
                return this.userName.split(' ').reduce((acc, curr) => {
                    if (acc.length >= 2) {
                        return acc;
                    }
                    let init = curr.charAt(0).toUpperCase();
                    return acc + init;
                }, "");
            },
            showImg() {
                return !!this.url || !!this.src && !this.errorOnLoadImg;
            },
            classes() {
                return {
                    'j-avatar': true,
                    'j-avatar--squad': this.squad,
                    [`elevation-${this.elevation}`]: true
                }
            }
        }
    }
</script>

<style lang="scss">
.j-avatar {
    color: rgb(var(--v-theme-primary));
    user-select: none;

    &--squad {
        border-radius: 10px !important;
    }

    .j-image {
        object-fit: cover !important;
    }
}
</style>