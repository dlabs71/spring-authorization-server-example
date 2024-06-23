<template>
    <v-dialog max-width="500px" v-model="model" content-class="d-confirm-dialog">
        <v-card>
            <v-card-title>
                Вы уверены что хотите удалить свой аккаунт?
            </v-card-title>
            <v-card-text>
                <div>
                    Для подтверждения удаления аккаунта введите ваш email:
                </div>
                <div class="mt-3">
                    <j-text-field
                        v-model="email"
                        density="compact"
                        :hide-details="true"
                        label="Email"
                        name="email"
                    />
                </div>
            </v-card-text>
            <v-card-actions>
                <v-row>
                    <v-row justify="end" class="mr-2">
                        <v-btn variant="plain" color="primary" @click="cancelHandler" class="mr-2">
                            Отмена
                        </v-btn>
                        <v-btn variant="outlined" color="error" @click="confirmHandler">
                            Удалить
                        </v-btn>
                    </v-row>
                </v-row>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
    import {ref, watch} from "vue";
    import {useStore} from "vuex";
    import {showENotify} from "@/global/functions/notification-funcs";

    const store = useStore();

    const email = ref(null);
    const model = defineModel();
    const emits = defineEmits(['confirm']);

    function close() {
        model.value = false;
    }

    function confirmHandler() {
        if (!email.value) {
            showENotify("Введите ваш email адрес, привязанный к текущему аккаунту");
            return;
        }
        let authUserEmail = store.getters.getAuthUserEmail;
        if (!!authUserEmail && authUserEmail === email.value) {
            close();
            emits('confirm');
        } else {
            showENotify("Введён не верный Email");
        }
    }

    function cancelHandler() {
        close();
        emits('cancel');
    }

    watch(() => model.value, () => {
        email.value = null;
    })

</script>


<style scoped lang="scss">
.j-confirm-dialog {
    .v-card-title {
        white-space: break-spaces;
    }
}
</style>