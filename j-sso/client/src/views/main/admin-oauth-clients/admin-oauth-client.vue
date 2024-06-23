<template>
    <main-card :header="header" :arrow-back="true" class="admin-oauth-client">
        <Form id="oauth-client-form"
              :validation-schema="schema"
              as="form"
              @submit="save">
            <v-row>
                <v-col>
                    <j-text-field
                        label="Client ID"
                        name="clientId"
                        v-model="dto.clientId"
                        :disabled="editMode"
                    />
                </v-col>
                <v-col>
                    <v-btn
                        class="change-secret-btn"
                        v-if="editMode"
                        variant="outlined"
                        @click="refreshClientSecret"
                        color="primary"
                    >
                        <v-icon icon="refresh"/>
                        Обновить Secret
                    </v-btn>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <j-text-field
                        label="Наименование"
                        name="clientName"
                        v-model="dto.clientName"
                    />
                </v-col>
                <v-col>
                    <j-date-picker
                        label="Дата действия"
                        name="clientSecretExpiresAt"
                        v-model="dto.clientSecretExpiresAt"
                        :min-date="new Date()"
                    />
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <j-multiselect
                        label="Метод аутентификации"
                        name="clientAuthenticationMethods"
                        :items="authMethods"
                        v-model="dto.clientAuthenticationMethods"
                    />
                </v-col>
                <v-col>
                    <j-multiselect
                        label="Grant Types"
                        name="authorizationGrantTypes"
                        :items="grantTypes"
                        v-model="dto.authorizationGrantTypes"
                    />
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <j-multiselect
                        label="Scopes"
                        name="scopes"
                        :items="scopes"
                        v-model="dto.scopes"
                    />
                </v-col>
            </v-row>
            <v-row>
                <v-divider/>
                <v-col>
                    <changeable-list
                        label="Redirect URI"
                        name="redirectUris"
                        v-model="dto.redirectUris"
                    />
                </v-col>
            </v-row>
            <v-row>
                <v-divider/>
                <v-col>
                    <changeable-list
                        label="Delete Notify URI"
                        name="deleteNotifyUris"
                        v-model="dto.deleteNotifyUris"
                    />
                </v-col>
            </v-row>
            <v-row justify="end">
                <v-btn
                    v-if="editMode"
                    variant="plain"
                    color="primary"
                    class="mr-2"
                    @click="cancel"
                >
                    Отмена
                </v-btn>
                <v-btn
                    variant="outlined"
                    color="primary"
                    class="mr-2 ml-2"
                    form="oauth-client-form"
                    type="submit"
                >
                    Сохранить
                </v-btn>
            </v-row>
        </Form>
        <client-secret-dialog
            v-if="clientId"
            :client-id="clientId"
            v-model="clientSecretDialogOpen"
        />
    </main-card>
</template>

<script setup>

    import MainCard from "@/views/main/components/main-card.vue";
    import {useRoute, useRouter} from "vue-router";
    import {onMounted, ref, watch} from "vue";
    import ChangeableList from "@/views/main/admin-oauth-clients/components/changeable-list.vue";
    import Service from './service/oauth-client-service';
    import ReferenceService from "@/global/service/reference-service";
    import {OauthClientModel} from "@/views/main/admin-oauth-clients/service/oauth-client-model";
    import {defineRule, Form} from 'vee-validate';
    import {showSNotify, showWNotify} from "@/global/functions/notification-funcs";
    import ClientSecretDialog from "@/views/main/admin-oauth-clients/dialogs/client-secret-dialog.vue";

    const router = useRouter();
    const route = useRoute();

    const header = ref("OAuth2 клиент: ");
    const authMethods = ref([]);
    const grantTypes = ref([]);
    const scopes = ref([]);
    const clientId = ref(null);
    const editMode = ref(false);
    const dto = ref(new OauthClientModel());
    const dtoBeforeEdit = ref(new OauthClientModel());
    const clientSecretDialogOpen = ref(false);

    const schema = {
        clientId: "clientIdRule",
        clientName: "required|max:200",
        clientAuthenticationMethods: "required",
        authorizationGrantTypes: "required",
        scopes: "required",
        redirectUris: "required"
    };
    defineRule('clientIdRule', (value, params, ctx) => {
        if (!value || !value.length) {
            return `Поле ${ctx.label} обязательно для заполнения`;
        }
        if (value.length > 100) {
            return "Максимум 100 символов";
        }
        if (!/^[a-z0-9-_]*$/ig.test(value)) {
            return `В поле разрешены только символы: A-Z a-z -_ 0-9`;
        }
        return true;
    });

    let cancel = () => {
        dto.value = dtoBeforeEdit.value.$clone();
    };

    function setUpClientData(result) {
        dto.value = result;
        dtoBeforeEdit.value = result.$clone();
    }

    function load() {
        return Service.get(route.params.clientId)
            .then(result => {
                setUpClientData(result);
            });
    }

    function loadReferences() {
        return Promise.all([
            ReferenceService.getAuthMethods(),
            ReferenceService.getGrantTypes(),
            ReferenceService.getScopes()
        ]).then(results => {
            authMethods.value = results[0];
            grantTypes.value = results[1];
            scopes.value = results[2];
        });
    }

    function save() {
        if (dtoBeforeEdit.value.$equals(dto.value)) {
            showWNotify("Нет изменений для сохранения");
            return;
        }
        return Service.save(dto.value).then(result => {
            showSNotify("Данные успешно сохранены");
            if (!editMode.value) {
                router.replace({
                    name: 'admin-oauth-client-new', params: {
                        clientId: result.clientId
                    }
                });
            } else {
                setUpClientData(result);
            }
        });
    }

    function refreshClientSecret() {
        clientSecretDialogOpen.value = true;
    }

    function prepareForm() {
        let routeClientIdParam = route.params.clientId;
        if (routeClientIdParam === 'new') {
            header.value = "Создание нового OAuth2 клиента";
            editMode.value = false;
            loadReferences();
            return;

        }
        clientId.value = route.params.clientId;
        header.value = "OAuth2 клиент: " + route.params.clientId;
        editMode.value = true;
        loadReferences().then(() => {
            load().then(() => {
                if (route.name === 'admin-oauth-client-new') {
                    refreshClientSecret();
                }
            })
        });
    }

    watch(() => clientSecretDialogOpen.value, (newValue) => {
        if (!newValue && route.name === 'admin-oauth-client-new') {
            router.replace({
                name: 'admin-oauth-client', params: {
                    clientId: dto.value.clientId
                }
            });
        }
    })

    onMounted(() => {
        prepareForm();
    });

    router.afterEach((to, from) => {
        if (to.name === 'admin-oauth-client-new'
            || (to.name === 'admin-oauth-client'
                && !!to.params.clientId
                && to.params.clientId !== from.params.clientId)
        ) {
            prepareForm();
        }
    })

</script>

<style scoped lang="scss">
.admin-oauth-client {
    .change-secret-btn {
        height: 56px;
        width: 100%;
    }
}
</style>