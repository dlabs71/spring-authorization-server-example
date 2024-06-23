<template>
    <v-list-item class="oauth-client-list-item elevation-2 ma-2" active-class="elevation-3">
        <v-list-item-title>
            {{ title }}
        </v-list-item-title>
        <v-list-item-subtitle>
            {{ subtitle }}
        </v-list-item-subtitle>

        <template v-slot:append>
            <v-btn
                icon="edit"
                @click="edit"
                color="primary"
                variant="plain"
                size="30"
                class="mr-3"
            />
            <v-btn
                icon="delete"
                @click="drop"
                color="error"
                variant="plain"
                size="30"
            />
        </template>
    </v-list-item>
</template>

<script setup>

    import {computed, defineEmits, defineProps} from 'vue';
    import {OauthClientModel} from "../service/oauth-client-model";

    const props = defineProps({
        dto: {
            type: OauthClientModel,
            required: true
        }
    });
    const emits = defineEmits(['edit', 'delete']);

    const title = computed(() => `${props.dto.clientName} (${props.dto.clientId})`);
    const subtitle = computed(() => props.dto.redirectUris[0]);

    function edit() {
        emits('edit', props.dto);
    }

    function drop() {
        emits('delete', props.dto);
    }

</script>

<style scoped lang="scss">
.oauth-client-list-item {

}
</style>