<template>
    <VueDatePicker
        v-model="model"
        locale="ru"
        :enableTimePicker="false"
        :autoApply="true"
        :teleport="true"
        allow-prevent-default
        menu-class-name="j-date-picker-menu"
        ref="menu"
        :min-date="minDate"
        :max-date="maxDate"
    >
        <template #trigger>
            <v-text-field
                :readonly="true"
                :model-value="formattedValue"
                :append-inner-icon="appendInnerIcon"
                :error-messages="errors"
                :label="label"
                variant="outlined"
                @blur="handleBlur"
                v-bind="$attrs"
                @keydown.delete="clearValue"
            />
        </template>
    </VueDatePicker>
</template>

<script>
    export default {
        name: "j-date-picker",
        inheritAttrs: false
    }
</script>

<script setup>
    import {computed, defineProps, ref, toRef, watch, defineEmits, defineModel} from 'vue';
    import {useField} from 'vee-validate';
    import VueDatePicker from '@vuepic/vue-datepicker';
    import '@vuepic/vue-datepicker/dist/main.css'
    import {beautyFormatDate, formatDate, formatDateFromStr} from "@/global/functions/date-helper";

    const props = defineProps({
        name: {
            type: String,
            required: true,
        },
        label: {
            type: String,
            required: true,
        },
        appendInnerIcon: {
            type: String
        },
        minDate: {},
        maxDate: {}
    });
    let model = defineModel();

    const emits = defineEmits(['update:modelValue']);

    const {value, handleBlur, errors} = useField(toRef(props, 'name'), undefined, {
        label: toRef(props, 'label')
    });

    let menu = ref(null);
    let clearValue = () => {
        menu.value.clearValue();
    };

    let onInput = (val) => {
        if (val === model.value) {
            return;
        }
        value.value = val;
        emits('update:modelValue', val);
    };

    // computed
    let formattedValue = computed(() => formatDateFromStr(model.value));

    watch(() => model.value, () => {
        value.value = model.value;
    });

</script>

<style lang="scss">
.j-date-picker-menu {
    .dp__button {
        display: none;
    }
}
</style>