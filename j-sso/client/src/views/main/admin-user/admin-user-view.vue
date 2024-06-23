<template>
    <main-card header="Администраторы" class="admin-user-view">
        <Form
            ref="emailForm"
            id="email-form"
            :validation-schema="schema"
            as="form"
            @submit="assign">
            <v-row align-content="center" class="assign-section pr-3 pl-3 mt-2">
                <v-col cols="12" sm="7" lg="8">
                    <j-text-field
                        label="Email пользователя"
                        name="email"
                        v-model="userEmail"
                    />
                </v-col>
                <v-col cols="12" sm="5" lg="4">
                    <v-btn color="primary"
                           class="assign-btn"
                           variant="outlined"
                           form="email-form"
                           type="submit">
                        Назначить <br/> администратором
                    </v-btn>
                </v-col>
            </v-row>
        </Form>
        <v-divider class="mt-5"/>
        <v-row justify="center">
            <v-col cols="12">
                <v-list>
                    <v-list-subheader>Администраторы системы</v-list-subheader>
                    <admin-user-list-item
                        v-for="(item, index) in users"
                        :key="`oauth-client-${index}`"
                        :dto="item"
                        @dismiss="dismiss"
                    />
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
    </main-card>
</template>

<script setup>
    import {computed, onMounted, ref} from "vue";
    import Service from "./service/admin-user-service";
    import MainCard from "@/views/main/components/main-card.vue";
    import AdminUserListItem from "./components/admin-user-list-item.vue";
    import {showSNotify, showWNotify} from "@/global/functions/notification-funcs";
    import {defineRule, Form} from 'vee-validate';

    const emailForm = ref();
    const users = ref();
    const page = ref(1);
    const pageSize = ref(10);
    const allItemsCont = ref(0);
    const confirmDialog = ref({
        header: "",
        onConfirm: () => {
        },
        onCancel: () => {
        }
    });
    const confirmDialogOpen = ref(false);
    const userEmail = ref(null);
    const errors = ref(null);
    const schema = {
        email: "email_required|max:100",
    };
    defineRule('email_required', value => {
        if (!value) {
            return 'Введите email адрес';
        }
        return true;
    });

    function search(pageVal) {
        if (pageVal !== null && pageVal !== undefined) {
            page.value = pageVal;
        } else {
            pageVal = page.value;
        }

        return Service.search(pageVal - 1, pageSize.value)
            .then(result => {
                users.value = result.data;
                allItemsCont.value = result.total;
            });
    }

    function assign(values, {resetForm}) {
        Service.assign(userEmail.value).then(() => {
            showSNotify("Пользователю успешно назначена роль администратора");
            userEmail.value = null;
            errors.value = null;
            search().finally(() => {
                resetForm();
            });
        });
    }

    function dismiss(dto) {
        if (!!users.value && users.value.length === 1) {
            showWNotify("Должен быть хотя бы один администратор");
            return;
        }
        confirmDialog.value = {
            header: `Вы уверенны что хотите снять роль администратора у пользователя: ${dto.userFullName}?`,
            onConfirm: () => {
                Service.dismiss(dto.id).then(() => {
                    showSNotify(`Роль администратора успешно снята с пользователя ${dto.userFullName}`);
                    search();
                });
            },
            onCancel: () => {
            }
        };
        confirmDialogOpen.value = true;
    }

    const countPages = computed(() => Math.ceil(allItemsCont.value / pageSize.value));

    onMounted(() => {
        search();
    });
</script>

<style scoped lang="scss">
.admin-user-view {
    .assign-section {
        align-content: center;

        .assign-btn {
            height: 56px;
            width: 100%;
        }
    }
}
</style>