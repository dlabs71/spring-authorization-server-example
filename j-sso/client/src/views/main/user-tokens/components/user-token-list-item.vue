<template>
    <v-expansion-panel class="user-token-list-item ma-2">
        <v-expansion-panel-title>
            <div class="mt-2">
                <div class="header text-h6">{{ title }}</div>
                <div class="text-subtitle-1 ml-2">{{ createDateSubtitle }}</div>
                <div class="text-subtitle-1 ml-2">{{ refreshDateSubtitle }}</div>
            </div>
        </v-expansion-panel-title>
        <v-expansion-panel-text class="ml-5">
            <div class="text-subtitle-2 mb-2">
                Тип авторизации: {{ grantTypeName }}
            </div>
            <div class="text-subtitle-2">
                URL перенаправления: {{ redirectUrl }}
            </div>
            <v-divider/>
            <v-list>
                <v-list-subheader>Доступы</v-list-subheader>
                <v-list-item
                    v-for="(item, index) in scopes"
                    :key="`scope-${index}`"
                >
                    <v-list-item-title>{{ item }}</v-list-item-title>
                </v-list-item>
            </v-list>
            <v-row justify="end" class="mb-1 mr-1">
                <v-btn
                    @click="recall"
                    color="error"
                    variant="outlined"
                >
                    Отозвать
                </v-btn>
            </v-row>
        </v-expansion-panel-text>
    </v-expansion-panel>
</template>

<script setup>

    import {computed, defineProps} from 'vue';
    import {UserTokensModel} from "../service/user-tokens-model";
    import {formatDateTimeFromStr} from "@/global/functions/date-helper";

    const props = defineProps({
        dto: {
            type: UserTokensModel,
            required: true
        }
    });
    const emits = defineEmits(['recall']);

    const title = computed(() => props.dto.clientName);
    const createDateSubtitle = computed(() => 'Дата авторизации: ' + formatDateTimeFromStr(props.dto.startDate));
    const refreshDateSubtitle = computed(() => 'Дата обновления токена: ' + formatDateTimeFromStr(props.dto.lastRefreshDate));
    const grantTypeName = computed(() => props.dto.grantType);
    const scopes = computed(() => props.dto.scopes);
    const redirectUrl = computed(() => props.dto.clientRedirectUri);

    function recall() {
        emits('recall', props.dto);
    }

</script>

<style scoped lang="scss">
.user-token-list-item {
    .header {

    }
}
</style>