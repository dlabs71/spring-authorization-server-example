<template>
    <div class="step-form">
        <div v-if="!loading" class="otp-input-container">
            <v-otp-input
                ref="otpInput"
                v-model:value="confirmedCode"
                :conditionalClass="['one', 'two', 'three', 'four', 'five', 'six']"
                :num-inputs="6"
                :should-auto-focus="true"
                input-classes="otp-input"
                input-type="number"
                separator="-"
                @on-complete="submit"
            />
        </div>
        <div v-if="loading" class="otp-input-container">
            <v-progress-circular indeterminate/>
        </div>
        <div class="info">
            <slot name="info"/>
            <div v-if="!loading">
                <p v-if="timerShow">Отправить код повторно можно через: <strong>{{
                        repeatCodeDuration
                    }}</strong>
                    секунд</p>
                <v-btn v-if="!timerShow" class="repeat-code-btn" @click="resendCode">Отправить код повторно
                </v-btn>
            </div>
        </div>
    </div>
</template>

<script>
    import {Form} from 'vee-validate';
    import VOtpInput from "vue3-otp-input";

    export default {
        name: "confirm-form",
        components: {Form, VOtpInput}
    }
</script>

<script setup>
    import {defineEmits, ref, watch, computed, defineExpose, onMounted, defineProps} from "vue";
    import {useStore} from "vuex";

    const store = useStore();
    const emit = defineEmits(['submit', 'resend']);

    // props
    const props = defineProps({
        isShowed: {
            type: Boolean,
            required: true,
        }
    });

    // data
    const loading = ref(false);
    const confirmedCode = ref("");
    const repeatCodeDuration = ref(60);

    // computed
    let timerShow = computed(() => repeatCodeDuration.value > 0);

    // watch
    watch(() => props.isShowed, (value) => {
        if (value) {
            startWaiting();
        }
    });

    // methods
    let startWaiting = () => {
        repeatCodeDuration.value = 60;
        let intervalId = setInterval(() => {
            repeatCodeDuration.value--;

            if (repeatCodeDuration.value <= 0) {
                clearInterval(intervalId);
            }
        }, 1000);
    };
    let submit = () => {
        loading.value = true;
        emit('submit', confirmedCode.value);
        confirmedCode.value = "";
    };

    let resetState = () => {
        loading.value = false;
        repeatCodeDuration.value = 0;
    };
    let resendCode = () => {
        startWaiting();
        emit('resend');
    };

    onMounted(() => {
        if (props.isShowed) {
            startWaiting();
        }
    });

    defineExpose({resetState})
</script>

<style lang="scss" scoped>
.step-form {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    position: absolute;
    top: 0;
    left: 0;

    .otp-input-container {
        display: flex;
        flex-direction: row;
        margin: 20px 0 20px 0;
    }

    .info {
        text-align: center;
        margin-top: 30px;
    }

    .repeat-code-btn {
        width: 260px;
        color: white;
        font-weight: bold;

        background: linear-gradient(-45deg, #f91b4c, #fa8844);
        background-size: 400% 400%;
        animation: blink 3s ease infinite;

        .v-btn__content {
            margin-left: 18px;
        }
    }
}
</style>

<style lang="scss">
.otp-input {
    width: 40px;
    height: 40px;
    padding: 5px;
    margin: 0 10px;
    font-size: 20px;
    border-radius: 4px;
    border: 2px solid rgba(250, 136, 68, 0.5);
    text-align: center;
}

/* Background colour of an input field with value */
.otp-input.is-complete {
    background-color: #e4e4e4;
}

.otp-input::-webkit-inner-spin-button,
.otp-input::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

input::placeholder {
    font-size: 15px;
    text-align: center;
    font-weight: 600;
}
</style>