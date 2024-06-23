<template>
    <main-card header="Выданные токены пользователя">
        <v-row justify="center">
            <v-col cols="12">
                <v-row justify="center" v-if="isEmpty">
                    <div>У Вас нет выданных токенов</div>
                </v-row>
                <v-expansion-panels variant="accordion" v-else>
                    <user-token-list-item
                        v-for="(item, index) in tokens"
                        :key="`user-token-${index}`"
                        :dto="item"
                        @recall="recall"
                    />
                </v-expansion-panels>
            </v-col>
        </v-row>
        <j-confirmation-dialog
            v-model="confirmDialogOpen"
            :header="confirmDialog.header"
            @confirm="confirmDialog.onConfirm"
            @cancel="confirmDialog.onCancel"
        />
    </main-card>
</template>

<script setup>

    import MainCard from "@/views/main/components/main-card.vue";
    import UserTokenListItem from "@/views/main/user-tokens/components/user-token-list-item.vue";
    import {computed, onMounted, ref} from "vue";
    import Service from "./service/user-tokens-service";
    import {showSNotify} from "@/global/functions/notification-funcs";

    const tokens = ref([]);
    const confirmDialog = ref({
        header: "",
        onConfirm: () => {
        },
        onCancel: () => {
        }
    });
    const confirmDialogOpen = ref(false);

    function search() {
        return Service.search()
            .then(result => {
                tokens.value = result;
            });
    }

    const isEmpty = computed(() => tokens.value.length === 0);

    function recall(dto) {
        confirmDialog.value = {
            header: `Вы уверенны что хотите токен для: ${dto.clientName}?`,
            onConfirm: () => {
                Service.recall(dto.authorizationId).then(() => {
                    showSNotify("Токен успешно отозван");
                    search();
                });
            },
            onCancel: () => {
            }
        };
        confirmDialogOpen.value = true;
    }

    onMounted(() => {
        search();
    });
</script>

<style scoped lang="scss">

</style>