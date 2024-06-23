<template>
    <v-dialog max-width="500px" v-model="model" content-class="j-confirm-dialog">
        <v-card>
            <v-card-title>
                {{ header }}
            </v-card-title>
            <v-card-text>
                {{ description }}
            </v-card-text>
            <v-card-actions>
                <v-row>
                    <v-row justify="end" class="mr-2">
                        <v-btn variant="plain" color="primary" @click="cancelHandler" class="mr-2">
                            {{ labelCancelBtn }}
                        </v-btn>
                        <v-btn variant="outlined" color="primary" @click="confirmHandler">
                            {{ labelConfirmBtn }}
                        </v-btn>
                    </v-row>
                </v-row>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    export default {
        name: "j-confirmation-dialog"
    }
</script>

<script setup>

    const props = defineProps({
        header: {
            type: String,
            required: true
        },
        description: {
            type: String
        },
        labelConfirmBtn: {
            type: String,
            default: "ОК"
        },
        labelCancelBtn: {
            type: String,
            default: "Отмена"
        }
    });

    const model = defineModel();

    const emits = defineEmits(['confirm', 'cancel']);

    function close() {
        model.value = false;
    }

    function confirmHandler() {
        close();
        emits('confirm');
    }

    function cancelHandler() {
        close();
        emits('cancel');
    }

</script>


<style scoped lang="scss">
.j-confirm-dialog {
    .v-card-title {
        white-space: break-spaces;
    }
}
</style>