<template>
    <Form id="init-reset-password"
          :validation-schema="schema"
          as="form"
          class="step-form"
          @submit="submit">
        <j-text-field
            v-model="dataForm.email"
            append-inner-icon="mail"
            class="mb-3"
            label="E-mail"
            name="email"
            type="email"
        />
        <v-btn
            :loading="loading"
            class="submit-btn"
            form="init-reset-password"
            type="submit"
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
}
</style>