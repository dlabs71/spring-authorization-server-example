<template>
    <main-card header="Профиль" class="profile-view">
        <Form id="profile-form"
              :validation-schema="schema"
              as="form"
              @submit="save">
            <v-row>
                <v-col cols="2" class="avatar-col">
                    <j-avatar-chooser
                        :user-name="fullUserName"
                        url="/account/avatar/current"
                        :src="avatarSrc"
                        @change="changeAvatar"
                    />
                </v-col>
                <v-col cols="10">
                    <div class="text-block">
                        <div class="label">E-mail</div>
                        <h4 class="value ma-2">{{ userData.email }}</h4>
                    </div>
                    <div class="text-block">
                        <div class="label">ID</div>
                        <h4 class="value ma-2">{{ userData.id }}</h4>
                    </div>
                </v-col>
            </v-row>
            <v-divider class="mt-10"/>
            <v-row class="mt-2">
                <v-col cols="12" md="6" lg="6">
                    <j-text-field
                        label="Фамилия"
                        name="lastName"
                        v-model="userData.lastName"
                    />
                </v-col>
                <v-col cols="12" md="6" lg="6">
                    <j-text-field
                        label="Имя"
                        name="firstName"
                        v-model="userData.firstName"
                    />
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="12" md="6" lg="6">
                    <j-text-field
                        label="Отчество"
                        name="middleName"
                        v-model="userData.middleName"
                    />
                </v-col>
                <v-col cols="12" md="6" lg="6">
                    <j-date-picker-flow
                        label="Дата рождения"
                        name="birthday"
                        v-model="userData.birthday"
                        :year-range="yearRange"
                    />
                </v-col>
            </v-row>
            <v-row justify="end">
                <v-btn variant="plain" color="primary" class="mr-2" @click="cancel">Отмена</v-btn>
                <v-btn variant="outlined"
                       color="primary"
                       class="mr-2 ml-2"
                       form="profile-form"
                       type="submit"
                >
                    Сохранить
                </v-btn>
            </v-row>
        </Form>
    </main-card>
</template>

<script>
    import {Form} from 'vee-validate';

    export default {
        name: "account-form",
        components: {Form}
    }
</script>

<script setup>
    import Service from '../service/account-service';
    import {computed, onMounted, ref} from "vue";
    import {UserModel} from "../service/account-model";
    import {useStore} from "vuex";
    import {useRouter} from "vue-router";
    import MainCard from "@/views/main/components/main-card.vue";
    import {showSNotify, showWNotify} from "@/global/functions/notification-funcs";
    import LoginService from "@/views/sign-view/login/service/login-service";

    const store = useStore();
    const router = useRouter();

    // data
    let userData = ref(new UserModel());
    let userDataBeforeEdit = ref(new UserModel());
    let newAvatarData = ref(null);

    let date = ref(null);
    const schema = {
        firstName: "required|max:100",
        lastName: "required|max:100",
        middleName: "max:100",
        birthday: "required"
    };

    //methods
    let changeAvatar = (data) => {
        newAvatarData.value = data;
    };
    let cancel = () => {
        userData.value = userDataBeforeEdit.value.$clone();
        newAvatarData.value = null;
    };

    function load() {
        Service.getCurrentUser().then(result => {
            setUpUserData(result);
        });
    }

    function setUpUserData(result) {
        userData.value = result;
        userDataBeforeEdit.value = result.$clone();
        newAvatarData.value = null;
    }

    function save() {
        if (userDataBeforeEdit.value.$equals(userData.value) && !newAvatarData.value) {
            showWNotify("Нет изменений для сохранения");
            return;
        }
        Service.saveCurrentUser(
            userData.value,
            !!newAvatarData.value ? newAvatarData.value.file : null
        ).then((result) => {
            showSNotify("Данные успешно сохранены");
            setUpUserData(result);
            LoginService.getCurrentUser();
        });
    }


    // computed
    let fullUserName = computed(() => {
        let data = userData.value;
        return `${data.lastName} ${data.firstName}`.trim();
    });
    let email = computed(() => {
        return userData.value.email;
    });
    let id = computed(() => userData.value.id);
    let avatarSrc = computed(() => {
        if (!!newAvatarData.value && !!newAvatarData.value.value) {
            return newAvatarData.value.value;
        }
        return null;
    });
    const yearRange = computed(() => [1950, new Date().getFullYear() - 4]);


    onMounted(() => {
        load();
    })
</script>

<style scoped lang="scss">
.profile-view {

    .text-block {
        display: flex;
        align-items: center;

        .label {
            min-width: 50px;
        }
    }

    .avatar-col {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .v-col {
        padding: 5px !important;
    }

    .v-field__input {
        padding-top: 0 !important;
        padding-bottom: 0 !important;
    }
}
</style>