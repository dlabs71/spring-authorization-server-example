<template>
    <div class="step-form">
        <v-icon
            class="mb-5"
            color="success"
            size="112"
        >
            check_circle
        </v-icon>
        <h2>{{ title }}</h2>
        <div class="content">
            <slot name="default"/>
        </div>
        <v-btn v-if="showLoginBtn" class="submit-btn mt-5" @click="goToLogin">
            Войти
        </v-btn>
    </div>
</template>

<script>
    import {Form} from 'vee-validate';

    export default {
        name: "success-form",
        components: {Form}
    }
</script>

<script setup>

    import {useRouter} from "vue-router";
    import {useStore} from "vuex";
    import {defineEmits} from "vue";

    const emit = defineEmits(['onLogin']);
    const router = useRouter();
    const store = useStore();

    // computed
    const props = defineProps({
        title: {
            type: String
        },
        showLoginBtn: {
            type: Boolean,
            default: false
        }
    })

    // methods
    let goToLogin = () => {
        router.replace({name: "login"});
        emit('onLogin');
    };
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

    .icon-wrapper {
        width: 100px;
        height: 100px;
        border-radius: 50%;
        background-color: green;
        display: flex;
        align-items: center;
        justify-content: center;

        .v-icon {
            color: white;
            font-size: 50px;
        }
    }

    h3 {
        margin-bottom: 30px;
        margin-top: 30px;
    }

    .content {
        text-align: center;
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
}
</style>