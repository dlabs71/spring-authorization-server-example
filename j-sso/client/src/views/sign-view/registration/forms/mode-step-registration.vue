<template>
    <div class="mode-step-registration">
        <client-oauth2-form/>
        <j-section-divider/>
        <div class="bottom-section">
            <div class="description">Вы также можете пройти регистрацию используя специальную форму. Для этого нажмите
                на кнопку ниже.
            </div>
            <v-btn class="submit-btn" rounded="xl" variant="outlined" @click="submit">Пройти регистрацию</v-btn>
        </div>
    </div>
</template>

<script>
    import ClientOauth2Form from "../../components/client-oauth2-form/client-oauth2-form.vue";

    export default {
        name: "mode-step-registration",
        components: {ClientOauth2Form}
    }
</script>

<script setup>
    import {defineEmits} from "vue";

    const emit = defineEmits(['next']);
    // methods
    let submit = () => {
        emit('next');
    };
</script>

<style lang="scss" scoped>
.mode-step-registration {

    .bottom-section {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        margin-top: 50px;

        .description {
            margin-bottom: 10px;
            text-align: center;
        }

        .submit-btn {
            color: #2f2f2f;
        }
    }
}
</style>