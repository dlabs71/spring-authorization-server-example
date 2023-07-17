<template>
    <form-wrapper>
        <h2>Добро пожаловать</h2>
        <Form :validation-schema="schema" as="form" @submit="login">
            <div class="manual-enter">
                <d-text-field
                        append-inner-icon="mail"
                        label="E-mail"
                        name="email"
                        type="email"
                        class="mb-3"
                        v-model="username"
                />
                <d-text-field
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
        
        <div class="divider">
            <span class="divider-line"></span>
            <span class="text">или</span>
            <span class="divider-line"></span>
        </div>
        
        <div class="btn-container">
            <v-btn class="enter-btn" icon variant="text" @click="()=>loginWith('yandex')">
                <img :src="yandexIcon"/>
            </v-btn>
            <v-btn class="enter-btn" icon variant="text" @click="()=>loginWith('google')">
                <img :src="googleIcon"/>
            </v-btn>
            <v-btn class="enter-btn" icon variant="text" @click="()=>loginWith('github')">
                <img :src="githubIcon"/>
            </v-btn>
        </div>
    </form-wrapper>
</template>

<script>

import {computed, ref} from "vue";
import LoginAPI from './service/login-service';
import {Form} from 'vee-validate';
import FormWrapper from "@/views/sign-view/components/form-wrapper";
import {useRouter} from "vue-router";
import {showENotify} from "@/global/functions/notification-funcs";

export default {
    name: "login-form",
    components: {FormWrapper, Form},
    setup() {
        const router = useRouter()
        let username = ref(null);
        let password = ref(null);
        let passwordType = ref("password");
        const schema = {
            email: 'required|email',
            password: "required"
        };

        let login = () => {
            LoginAPI.login(username.value, password.value).catch((response) => {
                if (response.status === 401) {
                    showENotify("Не верный логин или пароль");
                }
                showENotify("Произошла ошибка");
            });
        }

        let loginWith = (providerName) => {
            LoginAPI.loginWith(providerName);
        }

        let togglePasswordVisible = () => {
            if (passwordType.value === "password") {
                passwordType.value = "text";
            } else {
                passwordType.value = "password";
            }
        };

        return {
            // data
            googleIcon: require("@/assets/img/google.svg"),
            githubIcon: require("@/assets/img/github.svg"),
            yandexIcon: require("@/assets/img/yandex.svg"),
            username,
            password,
            passwordType,
            schema,
            // methods
            login,
            loginWith,
            togglePasswordVisible,
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
    
    .divider {
        display: flex;
        align-items: center;
        width: 100%;
        justify-content: center;
        margin: 20px 0;
        
        span {
            font-style: italic;
            color: #525252;
        }
        
        .text {
            margin: 0 10px;
        }
        
        .divider-line {
            height: 1px;
            width: 30%;
            background-color: #525252;
        }
    }
    
    .manual-enter {
        margin-top: 30px;
        display: flex;
        flex-direction: column;
        align-items: center;
        
        .v-text-field {
            width: 100%;
        }
        
        .login-btn {
            width: 260px;
            color: white;
            font-weight: bold;
            
            background: linear-gradient(-45deg, #f91b4c, #fa8844);
            background-size: 400% 400%;
            
            .v-btn__content {
                margin-left: 18px;
            }
        }
    }
    
    .btn-container {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .enter-btn {
            margin: 5px;
            text-transform: none !important;
            border-radius: 10px;
            
            img {
                width: 25px;
                height: 25px;
            }
        }
    }
</style>