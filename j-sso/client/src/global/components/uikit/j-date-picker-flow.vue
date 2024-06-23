<template>
    <VueDatePicker
        :model-value="value"
        @update:model-value="onInput"
        locale="ru"
        :flow="['year', 'month', 'calendar']"
        :enableTimePicker="false"
        :autoApply="true"
        :teleport="true"
        :year-range="yearRange"
        allow-prevent-default
        menu-class-name="j-date-picker-menu"
        ref="menu"
    >
        <template #trigger>
            <v-text-field
                :readonly="true"
                :model-value="formattedValue"
                :append-inner-icon="appendInnerIcon"
                :error-messages="errors"
                :label="label"
                :type="type"
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
        name: "j-date-picker-flow",
        inheritAttrs: false
    }
</script>

<script setup>
    import {computed, defineProps, ref, toRef, watch, defineEmits} from 'vue';
    import {useField} from 'vee-validate';
    import VueDatePicker from '@vuepic/vue-datepicker';
    import '@vuepic/vue-datepicker/dist/main.css'
    import {beautyFormatDate, formatDate, formatDateFromStr} from "@/global/functions/date-helper";

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
        },
        modelValue: {},
        yearRange: {}
    });

    const emits = defineEmits(['update:modelValue']);

    const {value, handleBlur, errors} = useField(toRef(props, 'name'), undefined, {
        label: toRef(props, 'label')
    });

    let menu = ref(null);
    let clearValue = () => {
        menu.value.clearValue();
    };

    watch(() => props.modelValue, () => {
        value.value = props.modelValue;
    });

    let onInput = (val) => {
        if (val === props.modelValue) {
            return;
        }
        console.log(val);
        value.value = val;
        emits('update:modelValue', val);
    };

    // computed
    let formattedValue = computed(() => beautyFormatDate(value.value));

</script>

<style lang="scss">
.j-date-picker-menu {
    .dp__button {
        display: none;
    }
}
</style>