<template>
    <main-card header="Активность">
        <v-row justify="center">
            <v-col cols="12">
                <v-list>
                    <user-event-list-item
                        v-for="(item, index) in events"
                        :key="`user-event-${index}`"
                        :dto="item"
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
    </main-card>
</template>

<script setup>

    import MainCard from "@/views/main/components/main-card.vue";
    import {computed, onMounted, ref} from "vue";
    import Service from "./service/user-events-service";
    import UserEventListItem from "./components/user-event-list-item.vue";

    const events = ref();
    const page = ref(1);
    const pageSize = ref(10);
    const allItemsCont = ref(0);

    function search(pageVal) {
        if (pageVal !== null && pageVal !== undefined) {
            page.value = pageVal;
        } else {
            pageVal = page.value;
        }

        return Service.search(pageVal - 1, pageSize.value)
            .then(result => {
                events.value = result.data;
                allItemsCont.value = result.total;
            });
    }

    const countPages = computed(() => Math.ceil(allItemsCont.value / pageSize.value));

    onMounted(() => {
        search();
    });
</script>

<style scoped lang="scss">

</style>