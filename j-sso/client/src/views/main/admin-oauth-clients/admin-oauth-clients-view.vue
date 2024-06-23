<template>
    <main-card header="OAuth2 клиенты" class="admin-oauth-clients">
        <Form id="search-form"
              :validation-schema="schema"
              as="form"
              @submit="()=>search()">
            <v-row class="ma-1">
                <v-col cols="12" sm="5">
                    <j-text-field label="Имя клиента" name="clientName" v-model="clientNameFilterValue"/>
                </v-col>
                <v-col cols="12" sm="5">
                    <j-text-field label="Client ID" name="clientId" v-model="clientIdFilterValue"/>
                </v-col>
                <v-col class="form-actions" cols="12" sm="2">
                    <v-btn
                        variant="outlined"
                        color="primary"
                        form="search-form"
                        type="submit"
                    >Поиск
                    </v-btn>
                </v-col>
            </v-row>
        </Form>
        <v-divider class="mt-4 mb-4"/>
        <v-row justify="center">
            <v-col cols="12">
                <v-list>
                    <oauth-client-list-item
                        v-for="(item, index) in clientList"
                        :key="`oauth-client-${index}`"
                        :dto="item"
                        @edit="openClientCard"
                        @delete="deleteClient"
                    />
                    <div class="text-center" v-if="clientList.length === 0">Нет данных</div>
                </v-list>
            </v-col>
        </v-row>
        <v-row justify="center">
            <v-pagination
                color="primary"
                :model-value="page"
                :length="countPages"
                rounded="circle"
                @update:model-value="search"
            ></v-pagination>
        </v-row>
        <j-confirmation-dialog
            v-model="confirmDialogOpen"
            :header="confirmDialog.header"
            @confirm="confirmDialog.onConfirm"
            @cancel="confirmDialog.onCancel"
        />
        <v-fab
            color="primary"
            icon="add"
            class="mr-4 mb-2"
            location="bottom end"
            absolute
            app
            appear
            @click="createNew"
        ></v-fab>
    </main-card>
</template>

<script setup>

    import MainCard from "@/views/main/components/main-card.vue";
    import Service from './service/oauth-client-service';
    import {computed, onMounted, ref} from "vue";
    import OauthClientListItem from "@/views/main/admin-oauth-clients/components/oauth-client-list-item.vue";
    import {useRouter} from "vue-router";
    import {showSNotify} from "@/global/functions/notification-funcs";
    import {defineRule, Form} from "vee-validate";

    const router = useRouter();

    const page = ref(1);
    const pageSize = ref(10);


    const clientList = ref([]);
    const allItemsCont = ref(0);

    const clientIdFilterValue = ref(null);
    const clientNameFilterValue = ref(null);
    const searchFilterBeforeEdit = ref(null);

    const confirmDialog = ref({
        header: "",
        onConfirm: () => {
        },
        onCancel: () => {
        }
    });
    const confirmDialogOpen = ref(false);
    const schema = {
        clientName: "search_field_max:200",
        clientId: "search_field_max:100"
    };
    defineRule('search_field_max', (value, [limit]) => {
        if (!!value && value.length > limit) {
            return `Максимум ${limit} символов`;
        }
        return true;
    });

    function search(pageVal) {
        if (pageVal !== null && pageVal !== undefined) {
            page.value = pageVal;
        } else {
            pageVal = page.value;
        }

        let filtersValue = `${clientIdFilterValue.value}|${clientNameFilterValue.value}|${pageVal}`;
        if (searchFilterBeforeEdit.value === filtersValue) {
            return Promise.resolve();
        }
        searchFilterBeforeEdit.value = filtersValue;

        return callSearchApi(pageVal);
    }

    function refresh(pageVal) {
        if (pageVal !== null && pageVal !== undefined) {
            page.value = pageVal;
        } else {
            pageVal = page.value;
        }
        return callSearchApi(pageVal);
    }

    function callSearchApi(pageVal) {
        return Service.search(pageVal - 1, pageSize.value, clientIdFilterValue.value, clientNameFilterValue.value)
            .then(result => {
                clientList.value = result.data;
                allItemsCont.value = result.total;
            });
    }

    function openClientCard(dto) {
        router.push({
            name: 'admin-oauth-client', params: {
                clientId: dto.clientId
            }
        });
    }

    function deleteClient(dto) {
        let clientId = dto.clientId;
        confirmDialog.value = {
            header: `Вы уверенны что хотите удалить клиент: ${dto.clientName} (${clientId})?`,
            onConfirm: () => {
                Service.delete(clientId).then(() => {
                    showSNotify(`Клиент ${dto.clientName} (${clientId}) успешно удален`);
                    refresh();
                });
            },
            onCancel: () => {
            }
        };
        confirmDialogOpen.value = true;
    }

    function createNew() {
        router.push({
            name: 'admin-oauth-client', params: {
                clientId: 'new'
            }
        });
    }

    const countPages = computed(() => Math.ceil(allItemsCont.value / pageSize.value));

    onMounted(() => {
        search();
    });
</script>

<style scoped lang="scss">
.admin-oauth-clients {
    .form-actions {
        display: flex;
        align-items: flex-start;
        justify-content: center;

        .v-btn {
            height: 56px;
            width: 100%;
        }
    }
}
</style>