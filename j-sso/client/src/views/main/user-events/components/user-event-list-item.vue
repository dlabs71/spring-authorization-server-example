<template>
    <v-list-item class="user-event-list-item elevation-2 ma-2" active-class="elevation-3">
        <v-list-item-title>
            {{ title }}
        </v-list-item-title>
        <v-list-item-subtitle>
            {{ subtitle }}
        </v-list-item-subtitle>

        <template v-slot:append>
            <div>{{ creationDate }}</div>
        </template>
    </v-list-item>
</template>

<script setup>

    import {computed, defineProps} from 'vue';
    import {UserEventsModel} from "../service/user-events-model";
    import {formatDateTimeFromStr} from "@/global/functions/date-helper";

    const props = defineProps({
        dto: {
            type: UserEventsModel,
            required: true
        }
    });

    const title = computed(() => props.dto.eventTypeName);
    const subtitle = computed(() => {
        let data = "";
        if (!!props.dto.browser) {
            data += props.dto.browser + '; ';
        }
        if (!!props.dto.os) {
            data += props.dto.os + '; ';
        }
        if (!!props.dto.ipAddress) {
            data += ' IP: ' + props.dto.ipAddress;
        }
        return data;
    });
    const creationDate = computed(() => formatDateTimeFromStr(props.dto.creationDate));

</script>

<style scoped lang="scss">
.user-event-list-item {

}
</style>