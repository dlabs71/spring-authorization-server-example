<template>
    <div class="changeable-list">
        <div class="edit-section">
            <j-text-field
                v-model="inputText"
                :label="label"
                :name="nameTextField"
                append-icon="add"
                @click:append="addNew"
                :error-messages="errors"
                @blur="handleBlur"
            />
        </div>
        <div class="list-section">
            <v-list>
                <v-list-item
                    v-for="(item, index) in value"
                    :key="`changeable-list-item-${index}`"
                    class="mt-2 mb-2 mr-10 ml-10"
                    variant="outlined"
                    density="compact"
                    nav
                    color="primary"
                    base-color="primary"
                >
                    <v-list-item-title>
                        {{ item }}
                    </v-list-item-title>
                    <template v-slot:append>
                        <v-btn
                            icon="delete"
                            @click="()=>drop(index)"
                            color="error"
                            variant="plain"
                            size="30"
                        />
                    </template>
                </v-list-item>
            </v-list>
        </div>
    </div>
</template>

<script setup>

    import {defineModel, defineProps, ref, toRef, watch} from "vue";
    import {useField} from "vee-validate";
    import {showWNotify} from "@/global/functions/notification-funcs";
    import {v4 as uuidv4} from 'uuid';

    const props = defineProps({
        name: {
            type: String,
            required: true,
        },
        label: {
            type: String,
            required: true,
        },
        disabled: {
            type: Boolean,
            default: false
        }
    });
    const model = defineModel();
    const inputText = ref(null);
    const nameTextField = uuidv4();

    function addNew() {
        if (!inputText.value) {
            showWNotify("Введите значение в поле, затем нажмите на +, чтобы добавить.");
            return;
        }
        try {
            new URL(inputText.value);
        } catch (err) {
            showWNotify("Ведённое значение не соответствует валидному URL адресу");
            return;
        }
        if (!model.value) {
            model.value = [];
        }
        let array = new Set(model.value);
        array.add(inputText.value);
        model.value = Array.from(array);

        inputText.value = null;
    }

    function drop(index) {
        if (!model.value) {
            return;
        }
        let array = model.value;
        array.splice(index, 1);
        model.value = Array.from(array);
    }

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

<style scoped lang="scss">
.changeable-list {

}
</style>