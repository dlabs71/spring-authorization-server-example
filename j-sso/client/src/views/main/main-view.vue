<template>
    <div class="main-view">
        <v-container class="main-container">
            <v-btn
                color="white"
                variant="plain"
                icon="menu"
                @click="drawer = !drawer"
                class="drawer-toggle d-block d-sm-block d-md-none"/>
            <v-navigation-drawer
                v-model="drawer"
                temporary
                class="nav-drawer d-block d-sm-block d-md-none"
            >
                <main-menu/>
            </v-navigation-drawer>
            <v-row class="row-container">
                <v-col cols="12" md="4" lg="3" class="d-none d-sm-none d-md-block">
                    <main-menu/>
                </v-col>
                <v-col cols="12" md="8" lg="9" class="scroll-box">
                    <router-view/>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
    import MainMenu from './components/main-menu.vue';

    export default {
        name: "main-view",
        components: {
            MainMenu
        },
        data() {
            return {
                drawer: false
            }
        }
    }
</script>

<style scoped lang="scss">
@import '~vuetify/lib/styles/settings/variables';

.main-view {
    min-height: 100vh;
    max-height: 100vh;
    min-width: 500px;
    background: linear-gradient(to bottom right, #5909ef 0%, #ac27f2 100%);
    padding-top: 50px;

    .drawer-toggle {
        position: absolute;
        top: 10px;
        left: 40px;
    }

    .main-container {
        width: 70%;
        height: calc(100vh - 15px);

        .nav-drawer {
            .menu-card {
                box-shadow: none !important;
                margin-left: 0 !important;
            }
        }

        .row-container {
            height: 100%;

            .scroll-box {
                height: calc(100% - 20px);
            }
        }

        .v-col {
            padding: 10px;
        }
    }

    .max-height {
        height: 100%;
    }

    @media #{map-get($display-breakpoints, 'md-and-down')} {
        .main-container {
            width: 95%;
        }
    }
}
</style>