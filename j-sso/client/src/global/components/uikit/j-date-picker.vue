<template>
    <VueDatePicker
        ref="menu"
        v-model="value"
        :autoApply="true"
        :enableTimePicker="false"
        :flow="['year', 'month', 'calendar']"
        :teleport="true"
        :year-range="yearRange"
        allow-prevent-default
        locale="ru"
        menu-class-name="j-date-picker-menu"
    >
        <template #trigger>
            <v-text-field
                :append-inner-icon="appendInnerIcon"
                :error-messages="errors"
                :label="label"
                :readonly="true"
                :type="type"
                :value="formattedValue"
                v-bind="$attrs"
                variant="outlined"
                @blur="handleBlur"
                @keydown.delete="clearValue"
            />
        </template>
    </VueDatePicker>
</template>

<script>
    export default {
        name: "j-date-picker"
    }
</script>

<script setup>
    import {computed, defineProps, ref, toRef} from 'vue';
    import {useField} from 'vee-validate';
    import VueDatePicker from '@vuepic/vue-datepicker';
    import '@vuepic/vue-datepicker/dist/main.css'
    import {beautyFormatDate} from "@/global/functions/date-helper-funcs";

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

    let menu = ref(null);
    let clearValue = () => {
        menu.value.clearValue();
    };


    // computed
    let formattedValue = computed(() => beautyFormatDate(value.value));
    let yearRange = computed(() => [1950, new Date().getFullYear() - 4]);

</script>

<style lang="scss">
.j-date-picker-menu {
    .dp__button {
        display: none;
    }
}
</style>