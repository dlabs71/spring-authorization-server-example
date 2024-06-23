<template>
    <v-list-item class="oauth-client-list-item elevation-2 ma-2" active-class="elevation-3">
        <template v-slot:prepend>
            <j-avatar
                :elevation="2"
                :size="40"
                :user-name="title"
                :url="avatarUrl"
            />
        </template>
        <v-list-item-title>
            {{ title }}
        </v-list-item-title>
        <v-list-item-subtitle>
            {{ subtitle }}
        </v-list-item-subtitle>

        <template v-slot:append>
            <v-btn
                v-if="!isSuperuser"
                icon="person_remove"
                @click="dismiss"
                color="error"
                variant="plain"
                size="30"
            />
        </template>
    </v-list-item>
</template>

<script setup>
    import {computed, defineProps} from 'vue';
    import {AdminUserModel} from "../service/admin-user-model";
    import Service from '../service/admin-user-service';

    const props = defineProps({
        dto: {
            type: AdminUserModel,
            required: true
        }
    });
    const emits = defineEmits(['dismiss']);

    const title = computed(() => `${props.dto.lastName} ${props.dto.firstName} ${props.dto.middleName || ""}`.trim());
    const subtitle = computed(() => props.dto.email);
    const avatarUrl = computed(() => {
        return Service.getAvatarUrl(props.dto.id);
    });

    function dismiss() {
        emits('dismiss', props.dto);
    }

    const isSuperuser = computed(() => props.dto.superuser);
</script>

<style scoped lang="scss">

</style>