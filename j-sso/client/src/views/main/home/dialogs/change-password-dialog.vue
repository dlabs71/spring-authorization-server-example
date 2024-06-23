<template>
    <v-dialog
        persistent
        :model-value="modelValue"
        max-width="500"
        content-class="change-password-dialog"
    >
        <v-card>
            <v-card-title>Смерить пароль</v-card-title>
            <v-card-text>
                <v-scroll-x-reverse-transition>
                    <password-form @submit="submitPassword" v-show="step === 'password'"/>
                </v-scroll-x-reverse-transition>
                <v-scroll-x-reverse-transition>
                    <confirm-form
                        :is-showed="true"
                        v-show="step === 'confirm'"
                        @resend="resendCode"
                        @submit="submitConfirm"
                        ref="confirmFormRef"
                    />
                </v-scroll-x-reverse-transition>
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

    import PasswordForm from "@/views/sign-view/components/password-form.vue";
    import ConfirmForm from "@/views/sign-view/components/confirm-form.vue";
    import Service from '../service/change-password-service';

    export default {
        components: {
            PasswordForm,
            ConfirmForm
        },
        emits: ['update:modelValue'],
        props: {
            modelValue: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                step: "password",
                passwordData: {}
            }
        },
        methods: {
            submitConfirm(code) {
                Service.confirm(code).then(() => {
                    this.close();
                    this.showSNotify("Пароль успешно изменён");
                });
            },
            close() {
                this.$emit('update:modelValue', false);
            },
            submitPassword(dataForm) {
                console.log("submit password: ", dataForm);
                this.passwordData = dataForm;
                Service.init(dataForm.password).then(() => {
                    this.step = "confirm";
                });
            },
            resetState() {
                this.$refs['confirmFormRef'].resetState();
            },
            resendCode() {
                console.log("resend code");
            }
        },
        watch: {
            modelValue(oldValue, newValue) {
                if (newValue) {
                    this.resetState();
                    this.step = 'password';
                    this.passwordData = {};
                }
            }
        }
    }
</script>

<style scoped lang="scss">
.change-password-dialog {

    .v-card {
        padding: 5px;
    }

    .v-card-text {
        position: relative;
        margin: 20px;
        min-height: 450px;
    }
}
</style>