<template>
    <v-dialog
        transition="dialog-bottom-transition"
        width="800px"
        v-model="switchOn"
    >
        <v-card>
            <v-toolbar
                color="warning"
                title="Возникла ошибка при работе сервиса"
            ></v-toolbar>
            <v-card-text>
                <div class="text-h5 pa-3">{{ description }}</div>
                <div class="stacktrace-block" v-if="showStacktraceBlock">
                    <p class="opender" @click="toggleStacktrace">
                        <v-icon>{{ stacktraceArrowIcon }}</v-icon>
                        Открыть детальное описание ошибки
                    </p>
                    <div class="stacktrace" v-if="stacktraceExpanded">
                        <p v-for="item in stacktrace">{{ item }}</p>
                    </div>
                </div>
            </v-card-text>
            <v-card-actions class="justify-end">
                <v-btn
                    variant="text"
                    @click="close"
                >Закрыть
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {mapGetters} from "vuex";

    export default {
        name: "j-exception",
        data() {
            return {
                switchOn: false,
                stacktraceExpanded: false
            }
        },
        methods: {
            close() {
                this.switchOn = false;
            },
            open() {
                this.switchOn = true;
            },
            toggleStacktrace() {
                this.stacktraceExpanded = !this.stacktraceExpanded;
            }
        },
        computed: {
            ...mapGetters({
                exceptionId: 'getExceptionId',
                status: 'getStatus',
                description: 'getDescription',
                stacktrace: 'getStacktrace',
                notificationId: 'getNotificationId',
                notificationLevel: 'getNotificationLevel',
                notificationMessage: 'getNotificationMessage'
            }),
            showStacktraceBlock() {
                return !!this.stacktrace && this.stacktrace.length > 0;
            },
            stacktraceArrowIcon() {
                if (this.stacktraceExpanded) {
                    return "arrow_drop_up";
                }
                return "arrow_drop_down";
            }
        },
        watch: {
            notificationId(value) {
                if (!!value) {
                    let level = this.notificationLevel;
                    if (!level) {
                        level = "error";
                    }
                    this.showNotify(this.notificationMessage, null, level.toLowerCase());
                }
            },
            switchOn(value) {
                if (!value) {
                    setTimeout(() => {
                        this.$store.dispatch("resetException");
                    }, 500);
                }
            },
            exceptionId(value) {
                if (!!value) {
                    this.open();
                } else {
                    this.close();
                }
            }
        }
    }
</script>

<style scoped lang="scss">
@import '@/assets/scss/index.scss';

.stacktrace-block {
    .opender {
        margin: 5px 15px;
        cursor: pointer;
        font-size: 12px;
        opacity: 0.5;
        transition: opacity .3s ease-out;
        width: fit-content;
        font-style: italic;

        &:hover {
            opacity: 1;
        }
    }

    .stacktrace {
        max-height: 500px;
        padding-left: 10px;
        padding-right: 10px;
        border-left: 1px solid gray;
        @extend .scroll-box;
    }
}
</style>