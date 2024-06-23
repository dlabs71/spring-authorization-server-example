<template>
    <form-wrapper>
        <h2>Добро пожаловать</h2>
        <Form :validation-schema="schema" as="form" @submit="login">
            <div class="manual-enter">
                <j-text-field
                    append-inner-icon="mail"
                    label="E-mail"
                    name="email"
                    type="email"
                    class="mb-3"
                    v-model="username"
                />
                <j-text-field
                    :append-inner-icon="cPasswordIcon"
                    :type="passwordType"
                    label="Пароль"
                    name="password"
                    @click:appendInner="togglePasswordVisible"
                    class="mb-3"
                    v-model="password"
                />
                <v-btn class="login-btn" type="submit">
                    Войти
                    <v-icon>arrow_forward</v-icon>
                </v-btn>
            </div>
        </Form>

        <j-section-divider/>
        <client-oauth2-form/>

        <div class="bottom-container">
            <div class="link" @click="goToRegistration">Зарегистрироваться</div>
            <div class="link" @click="goToResetPassword">Забыли пароль?</div>
        </div>
    </form-wrapper>
</template>

<script>

    import {computed, onMounted, ref} from "vue";
    import LoginAPI from './service/login-service';
    import {Form} from 'vee-validate';
    import FormWrapper from "@/views/sign-view/components/form-wrapper";
    import {useRouter} from "vue-router";
    import {useStore} from "vuex";
    import ClientOauth2Form from "../components/client-oauth2-form/client-oauth2-form.vue";
    import {showENotify} from "@/global/functions/notification-funcs";

    export default {
        name: "login-form",
        components: {ClientOauth2Form, FormWrapper, Form},
        setup() {
            const store = useStore();
            const router = useRouter();
            let username = ref(null);
            let password = ref(null);
            let passwordType = ref("password");
            const schema = {
                email: 'required|email',
                password: "required"
            };

            let login = () => {
                LoginAPI.login(username.value, password.value)
                    .catch(result => {
                        if (result.status === 401) {
                            showENotify("Не верный логин или пароль");
                            return;
                        }
                        return result;
                    });
            }

            let togglePasswordVisible = () => {
                if (passwordType.value === "password") {
                    passwordType.value = "text";
                } else {
                    passwordType.value = "password";
                }
            };

            let goToRegistration = () => {
                router.push("registration");
            };

            let goToResetPassword = () => {
                router.push("reset-password");
            };

            onMounted(() => {
                // reset state for registration process
                store.dispatch('setRegistrationData', null);
                store.dispatch('setRegistrationStep', 0);

                // reset state for forgot password process
                store.dispatch('setResetData', null);
                store.dispatch('setResetStep', 0);
            });

            return {
                // data
                username,
                password,
                passwordType,
                schema,
                // methods
                login,
                togglePasswordVisible,
                goToRegistration,
                goToResetPassword,
                // computed
                cPasswordIcon: computed(() => passwordType.value === 'password' ? "visibility" : "visibility_off")
            }
        }
    }
</script>

<style lang="scss" scoped>
form {
    width: 100%;
}

.manual-enter {
    margin-top: 30px;
    display: flex;
    flex-direction: column;
    align-items: center;

    .v-text-field {
        width: 100%;
    }


    @keyframes blink {
        0% {
            background-position: 0% 50%
        }
        50% {
            background-position: 100% 50%
        }
        100% {
            background-position: 0% 50%
        }
    }

    .login-btn {
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

.bottom-container {
    position: absolute;
    bottom: 20px;

    .link {
        align-self: center;
        margin: 5px auto 15px auto;
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
}
</style>