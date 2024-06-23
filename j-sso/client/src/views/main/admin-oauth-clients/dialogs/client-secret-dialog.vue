<template>
    <v-dialog
        persistent
        :model-value="modelValue"
        max-width="500"
        content-class="client-secret-dialog"
    >
        <v-card>
            <v-card-title>Secret</v-card-title>
            <v-card-text>
                <div class="secret-field">
                    <div class="text">
                        {{ secretValue }}
                    </div>
                    <v-btn
                        icon="content_copy"
                        size="40"
                        variant="plain"
                        color="primary"
                        @click="copyValueInBuffer"
                    />
                </div>
                <div class="information">
                    Обязательно скопируйте секретный ключ нового клиента. Вы не сможете увидеть его снова.
                </div>
            </v-card-text>
            <v-card-actions>
                <v-row justify="end" class="mr-2">
                    <v-btn variant="outlined" color="primary" @click="close">Закрыть</v-btn>
                </v-row>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    import Service from '../service/oauth-client-service';
    import {showSNotify} from "@/global/functions/notification-funcs";

    export default {
        emits: ['update:modelValue'],
        props: {
            modelValue: {
                type: Boolean,
                default: false
            },
            clientId: {
                type: String,
                required: true
            }
        },
        data() {
            return {
                secretValue: null
            }
        },
        methods: {
            close() {
                this.$emit('update:modelValue', false);
            },
            genSecret() {
                Service.genSecret(this.clientId)
                    .then((result) => {
                        this.secretValue = result.data;
                    });
            },
            resetState() {
                this.secretValue = null;
            },
            copyValueInBuffer() {
                navigator.clipboard.writeText(this.secretValue).then(() => {
                    showSNotify("Секретный ключ скопирован в буфер обмена");
                });
            }
        },
        watch: {
            modelValue(newValue, oldValue) {
                this.resetState();
                if (newValue) {
                    this.genSecret();
                }
            }
        }
    }
</script>

<style scoped lang="scss">
.client-secret-dialog {

    .v-card-text {
        position: relative;
        margin: 20px;
        padding: 0 !important;

        .secret-field {
            display: flex;

            .text {
                border: 1px solid rgb(var(--v-theme-primary));
                min-height: 40px;
                border-radius: 5px;
                width: calc(100% - 50px);
                white-space: pre-wrap;
                line-height: 30px;
                padding: 5px;
            }

            .v-btn {
                margin-left: 10px;
            }
        }

        .information {
            padding: 5px;
            margin-top: 20px;
        }
    }
}
</style>