<template>
    <v-select
        :label="label"
        multiple
        v-model="model"
        variant="outlined"
        :error-messages="errors"
        @blur="handleBlur"
    ></v-select>
</template>

<script>
    export default {
        name: 'j-multiselect',
        inheritAttrs: true
    }
</script>

<script setup>
    import {defineModel, defineProps, toRef, watch} from 'vue';
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