<template>
    <v-container class="main-card card-panel">
        <div class="header-section">
            <v-btn
                v-if="arrowBack"
                rounded
                variant="text"
                size="30"
                class="mr-2"
                @click="goBack"
            >
                <v-icon size="20">arrow_back</v-icon>
            </v-btn>
            <h3 :class="arrowBack ? null : 'ml-4'">{{ header }}</h3>
        </div>
        <div class="content-section">
            <slot name="default"/>
        </div>
    </v-container>
</template>

<script>
    export default {
        props: {
            header: {
                type: String,
                require: true
            },
            arrowBack: {
                type: Boolean,
                default: false
            }
        },
        methods: {
            goBack() {
                this.$router.back();
            }
        }
    }

</script>

<style scoped lang="scss">
.main-card {
    position: relative;

    .header-section {
        color: rgb(var(--v-theme-primary));
        border-left: rgb(var(--v-theme-primary)) solid 3px;
        display: flex;
        align-items: center;
        padding-left: 5px;
    }

    .content-section {
        padding-top: 30px;
    }
}
</style>