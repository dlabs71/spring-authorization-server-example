<template>
    <Form id="password-form"
          :validation-schema="schema"
          as="form"
          class="step-form"
          @submit="submit">
        <j-text-field
            v-model="password"
            :append-inner-icon="passwordIcon"
            :type="passwordTypeField"
            class="mb-3"
            label="Пароль"
            name="password"
            @click:appendInner="togglePasswordVisible"
        />
        <j-text-field
            v-model="repeatedPassword"
            :append-inner-icon="repeatedPasswordIcon"
            :type="repeatedPasswordTypeField"
            class="mb-3"
            label="Повторите пароль"
            name="repeatedPassword"
            @click:appendInner="toggleRepeatedPasswordVisible"
        />
        <v-btn
            :loading="loading"
            class="submit-btn"
            form="password-form"
            type="submit"
        >
            {{ btnLabel }}
            <v-icon>arrow_forward</v-icon>
        </v-btn>

        <div class="password-requires">
            <p>Пароль от вашего аккаунта должен соответствовать следующим требованиям:</p>
            <ul>
                <li>
                    <j-req-done-indicator :done="passCondition1"/>
                    не менее 10 символов;
                </li>
                <li>
                    <j-req-done-indicator :done="passCondition2"/>
                    должен содержать символы в верхнем и нижнем регистре;
                </li>
                <li>
                    <j-req-done-indicator :done="passCondition3"/>
                    допустимы только латинские буквы, арабские цифры и спец. символы;
                </li>
                <li>
                    <j-req-done-indicator :done="passCondition4"/>
                    как минимум одна цифра;
                </li>
                <li>
                    <j-req-done-indicator :done="passCondition5"/>
                    <span>как минимум один из спец. символов:</span>
                    <div class="special-symbols">{{ specialSymbols }}</div>
                </li>
            </ul>
        </div>
    </Form>
</template>

<script>
    import {Form} from 'vee-validate';

    export default {
        name: "password-form",
        components: {Form}
    }
</script>

<script setup>
    import {computed, defineEmits, ref, defineExpose} from 'vue';
    import {useStore} from "vuex";
    import {showENotify} from "@/global/functions/notification-funcs";

    const store = useStore();
    const props = defineProps({
        btnLabel: {
            type: String,
            default: "Далее"
        }
    });

    // data
    const specialSymbols = "~!?@#$%^&*_-+()[]{}/\\|\"'.,:;\<\>";
    let loading = ref(false);
    const emit = defineEmits(['submit']);
    let password = ref(null);
    let repeatedPassword = ref(null);
    const schema = {
        password: "required",
        repeatedPassword: "confirmed:password"
    };
    let passwordHide = ref(true);
    let repeatedPasswordHide = ref(true);

    // computed
    let storedRegistrationData = computed(() => store.getters.getRegistrationData);
    let passwordIcon = computed(() => passwordHide.value ? "visibility_off" : "visibility");
    let repeatedPasswordIcon = computed(() => repeatedPasswordHide.value ? "visibility_off" : "visibility");
    let passwordTypeField = computed(() => passwordHide.value ? "password" : "text");
    let repeatedPasswordTypeField = computed(() => repeatedPasswordHide.value ? "password" : "text");

    let passCondition1 = computed(() => {
        if (!!password.value) {
            return password.value.length >= 10;
        }
        return false;
    });
    let passCondition2 = computed(() => {
        if (!!password.value) {
            return /^.*[A-Z].*$/.test(password.value) && /^.*[a-z].*$/.test(password.value);
        }
        return false;
    });
    let passCondition3 = computed(() => {
        if (!!password.value) {
            return !/[^A-Za-z0-9~!?@#$%^&*_\-+()\[\]{}/|"'.,:;<>]/g.test(password.value);
        }
        return false;
    });
    let passCondition4 = computed(() => {
        if (!!password.value) {
            return /^.*[0-9].*$/.test(password.value);
        }
        return false;
    });
    let passCondition5 = computed(() => {
        if (!!password.value) {
            return /^.*[~!?@#$%^&*_\-+()\[\]{}/|"'.,:;<>].*$/.test(password.value);
        }
        return false;
    });


    // methods
    let submit = () => {
        if (password.value !== repeatedPassword.value) {
            showENotify("Пароли не совпадают");
            return;
        }
        if (!passCondition1.value
            || !passCondition2.value
            || !passCondition3.value
            || !passCondition4.value
            || !passCondition5.value
        ) {
            showENotify("Все требования к паролю должны быть выполнены");
            return;
        }
        loading.value = true;
        emit('submit', {
            password: password.value,
            repeatedPassword: repeatedPassword.value
        });
    };
    let resetState = () => {
        loading.value = false;
    };
    let setData = (pass, repeatedPass) => {
        password.value = pass;
        repeatedPassword.value = repeatedPass;
    };
    let togglePasswordVisible = () => {
        passwordHide.value = !passwordHide.value;
    };
    let toggleRepeatedPasswordVisible = () => {
        repeatedPasswordHide.value = !repeatedPasswordHide.value;
    };

    defineExpose({resetState, setData})
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

    .v-text-field {
        width: 100%;
    }

    .submit-btn {
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

    .password-requires {
        margin-top: 20px;
        font-size: 14px;

        p {
            font-weight: bold;
        }

        ul {
            list-style: none;
            margin-top: 10px;

            .v-icon {
                margin-right: 5px;
            }

            .special-symbols {
                margin-left: 30px;
            }
        }
    }
}
</style>