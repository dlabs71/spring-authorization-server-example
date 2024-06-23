<template>
    <main-card header="Действия" class="actions-card">
        <v-row class="ma-1">
            <v-btn
                variant="outlined"
                color="primary"
                @click="changePasswordDialog = true"
            >
                <v-icon class="mr-1">key</v-icon>
                Сменить пароль
            </v-btn>
            <v-btn
                v-if="!isSuperuser"
                class="delete-btn-section"
                variant="outlined"
                color="error"
                @click="deleteAccountBtnHandler"
            >
                <v-icon class="mr-1">person_remove</v-icon>
                Удалить аккаунт
            </v-btn>
        </v-row>
        <change-password-dialog
            v-model="changePasswordDialog"
        />
        <delete-account-dialog
            v-if="!isSuperuser"
            v-model="deleteAccountDialog"
            @confirm="deleteAccount"
        />
    </main-card>
</template>

<script setup>
    import MainCard from "@/views/main/components/main-card.vue";
    import {computed, ref} from "vue";
    import ChangePasswordDialog from "../dialogs/change-password-dialog.vue";
    import DeleteAccountDialog from "../dialogs/delete-account-dialog.vue";
    import Service from '../service/account-service';
    import {showENotify, showSNotify} from "@/global/functions/notification-funcs";
    import {useStore} from "vuex";
    import {useRouter} from "vue-router";
    import LoginService from "@/views/sign-view/login/service/login-service";

    const store = useStore();
    const router = useRouter();

    const changePasswordDialog = ref(false);
    const deleteAccountDialog = ref(false);

    function deleteAccountBtnHandler() {
        if (store.getters.isAdmin) {
            showENotify("Нельзя удалить аккаунт пока Вы являетесь администратором");
            return;
        }
        deleteAccountDialog.value = true;
    }

    function deleteAccount() {
        Service.deleteAccount().then(() => {
            showSNotify("Аккаунт успешно удален");
            LoginService.afterLogout();
        })
    }

    const isSuperuser = computed(() => store.getters.isSuperuser);
</script>

<style scoped lang="scss">
.pass-form {
    width: 90%;
}
</style>

<style lang="scss">
.actions-card {
    .delete-btn-section {
        margin-left: auto;
    }
}
</style>