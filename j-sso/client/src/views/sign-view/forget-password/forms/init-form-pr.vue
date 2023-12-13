<template>
    <Form id="init-reset-password"
          :validation-schema="schema"
          as="form"
          class="step-form"
          @submit="submit">
        <j-text-field
            append-inner-icon="mail"
            label="E-mail"
            name="email"
            type="email"
            class="mb-3"
            v-model="dataForm.email"
        />
        <v-btn
            class="submit-btn"
            type="submit"
            form="init-reset-password"
            :loading="loading"
        >
            Далее
        </v-btn>
    </Form>
</template>

<script>
import {Form} from 'vee-validate';

export default {
    name: "init-form-pr",
    components: {Form}
}
</script>

<script setup>
import {defineEmits, ref} from 'vue';
import {useStore} from "vuex";

const store = useStore();
const emit = defineEmits(['next']);

// data
const dataForm = {
    email: null
}
const schema = {
    email: "required|email"
};
let loading = ref(false);

// methods
let submit = () => {
    emit('next', dataForm);
    loading.value = true;
};
let resetState = () => {
    loading.value = false;
};
</script>

<style scoped lang="scss">
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
    }
</style>