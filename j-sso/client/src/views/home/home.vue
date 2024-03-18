<template>
    <v-container class="home-view">
        <h1>Главная страница</h1>
        <div class="info-block">
            <v-card
                class="mx-auto"
                max-width="500"
                variant="outlined"
            >
                <v-card-item>
                    <v-card-title>
                        <v-avatar color="red" class="mr-3">
                            <v-img
                                v-if="!!avatar"
                                :src="avatar"
                                :alt="fullUserName"
                            ></v-img>
                            <span v-else>{{ initialsUser }}</span>
                        </v-avatar>
                        {{ fullUserName }}
                    </v-card-title>
                    <v-card-subtitle class="pl-15">
                        Email: {{ email }}
                    </v-card-subtitle>
                    <v-card-subtitle class="pl-15">
                        ID: {{ id }}
                    </v-card-subtitle>
                    <v-divider/>
                    <div>
                        <h3>Доступные привилегии</h3>
                        <v-list variant="elevated">
                            <v-list-item class="ma-2" v-for="(item, index) in authorities">
                                <v-list-item-title>{{ index + 1 }}. {{ item }}</v-list-item-title>
                            </v-list-item>
                        </v-list>
                    </div>
                </v-card-item>
            </v-card>
        </div>
    </v-container>
</template>

<script>
    export default {
        name: "home"
    }
</script>

<script setup>
    import Service from './service/home-service';
    import LoginService from '../sign-view/login/service/login-service';
    import {computed, onMounted, ref} from "vue";
    import {UserModel} from "@/views/home/service/home-model";
    import {useStore} from "vuex";
    import {useRouter} from "vue-router";

    const store = useStore();
    const router = useRouter();
    let userData = ref(new UserModel());

    let fullUserName = computed(() => {
        let data = userData.value;
        return `${data.lastName} ${data.firstName} ${data.middleName || ""}`.trim();
    });
    let initialsUser = computed(() => {
        let data = userData.value;
        if (!data.lastName && !data.firstName) {
            return "";
        }
        console.log(data);
        return `${data.lastName.charAt(0)}${data.firstName.charAt(0)}`;
    })
    let email = computed(() => {
        return userData.value.email;
    });
    let id = computed(() => userData.value.id);
    let avatar = computed(() => userData.value.avatarUrl);
    let authorities = computed(() => userData.value.authorities);

    onMounted(() => {

        // Необходимо, когда в SSO авторизуются через сторонний сервис.
        // Смотри router.js
        if (!store.getters.isAuth) {
            LoginService.getCurrentUser();
            return;
        }
        Service.getCurrentUser().then(result => {
            userData.value = result;
            console.log(result);
        })
    })
</script>

<style scoped lang="scss">

</style>