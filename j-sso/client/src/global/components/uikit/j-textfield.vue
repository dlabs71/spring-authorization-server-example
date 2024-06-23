<template>
    <v-text-field
        v-model="model"
        :error-messages="errors"
        :label="label"
        variant="outlined"
        @blur="handleBlur"
    />
</template>

<script>
    export default {
        name: 'j-text-field',
        inheritAttrs: true
    }
</script>

<script setup>
    import {defineProps, toRef, watch, defineModel, onMounted} from 'vue';
    import {useField} from 'vee-validate';

    const props = defineProps({
        name: {
            type: String,
            required: true,
        },
        label: {
            type: String,
            required: true,
        }
    });

    const model = defineModel();
    const {value, handleBlur, errors} = useField(
        toRef(props, 'name'),
        undefined,
        {
            label: toRef(props, 'label')
        }
    );

    watch(() => model.value, () => {
        value.value = model.value;
    });
</script>

<style scoped>

</style>