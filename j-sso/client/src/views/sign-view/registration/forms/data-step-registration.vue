<template>
    <Form id="data-step-registration"
          :validation-schema="schema"
          as="form"
          class="step-form"
          @submit="submit">
        <j-text-field
            v-model="dataForm.email"
            append-inner-icon="mail"
            class="mb-1"
            label="E-mail"
            name="email"
            type="email"
        />
        <j-text-field
            v-model="dataForm.secondName"
            append-inner-icon="badge"
            class="mb-1"
            label="Фамилия"
            name="secondName"
        />
        <j-text-field
            v-model="dataForm.firstName"
            append-inner-icon="badge"
            class="mb-1"
            label="Имя"
            name="firstName"
        />
        <j-text-field
            v-model="dataForm.middleName"
            append-inner-icon="badge"
            class="mb-1"
            label="Отчество"
            name="middleName"
        />
        <j-date-picker
            v-model="dataForm.birthday"
            append-inner-icon="calendar_month"
            class="mb-1"
            label="Дата рождения"
            name="birthday"
        />
        <v-btn class="submit-btn" form="data-step-registration" type="submit">
            Далее
        </v-btn>
    </Form>
</template>

<script>
    import {Form} from 'vee-validate';

    export default {
        name: "data-step-registration",
        components: {Form}
    }
</script>

<script setup>
    import {computed, defineEmits, watch} from 'vue';
    import {useStore} from "vuex";
    import {STEPS} from "@/views/sign-view/registration/forms/constants";

    const store = useStore();
    const emit = defineEmits(['next']);

    // data
    const dataForm = {
        email: null,
        secondName: null,
        firstName: null,
        middleName: null,
        birthday: null
    }
    const schema = {
        email: "required|email",
        secondName: "required",
        firstName: "required",
        birthday: "required"
    };

    // computed
    let isShowed = computed(() => store.getters.getRegistrationStep === STEPS.DATA);
    let storedRegistrationData = computed(() => store.getters.getRegistrationData);

    // watch
    watch(() => isShowed.value, (value) => {
        if (value) {
            if (!!storedRegistrationData.value) {
                dataForm.email = storedRegistrationData.value.email;
                dataForm.secondName = storedRegistrationData.value.secondName;
                dataForm.firstName = storedRegistrationData.value.firstName;
                dataForm.middleName = storedRegistrationData.value.middleName;
                dataForm.birthday = storedRegistrationData.value.birthday;
            }
        }
    });

    // methods
    let submit = () => {
        emit('next', dataForm);
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