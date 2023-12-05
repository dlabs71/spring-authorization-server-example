<template>
    <v-text-field
        v-model="value"
        :append-inner-icon="appendInnerIcon"
        :error-messages="errors"
        :label="label"
        :type="type"
        v-bind="$attrs"
        variant="outlined"
        @blur="handleBlur"
    />
</template>

<script>
    export default {
        name: 'j-text-field',
        inheritAttrs: false
    }
</script>

<script setup>
    import {defineProps, toRef} from 'vue';
    import {useField} from 'vee-validate';

    const props = defineProps({
        name: {
            type: String,
            requird: true,
        },
        type: {
            type: String,
            requird: true,
        },
        label: {
            type: String,
            required: true,
        },
        appendInnerIcon: {
            type: String
        }
    });

    const {value, handleBlur, errors} = useField(toRef(props, 'name'), undefined, {
        label: toRef(props, 'label')
    });
</script>

<style scoped>

</style>