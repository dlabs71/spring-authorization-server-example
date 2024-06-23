<template>
    <input :accept="accept"
           hidden
           :disabled="disabled"
           id="icon-button-file"
           type="file"
           @change="setImage"/>
    <label for="icon-button-file">
        <v-badge
            icon="photo_camera"
            offset-x="7"
            offset-y="7"
            location="bottom right"
        >
            <j-avatar
                v-ripple
                :size="size"
                :elevation="elevation"
                :src="src"
                :userName="userName"
                :squad="squad"
                :url="url"
            />
        </v-badge>
    </label>
</template>

<script>
    export default {
        name: "j-avatar-chooser",
        emits: ["change"],
        props: {
            size: {
                type: Number,
                default: 70
            },
            elevation: {
                type: Number,
                default: 10
            },
            src: {
                type: String,
                default: null
            },
            url: {
                type: String,
                default: null
            },
            userName: {
                type: String,
                default: null
            },
            squad: {
                type: Boolean,
                default: false
            },
            accept: {
                type: String,
                default: "image/*"
            },
            disabled: {
                type: Boolean,
                default: false
            }
        },
        methods: {
            errorOnLoad() {
                this.errorOnLoadImg = true;
            },
            setImage(event) {
                event.preventDefault();
                let reader = new FileReader();
                let file = event.target.files[0];

                reader.onloadend = () => {
                    if (file.size > 3000000) {
                        this.showWNotify("Изображение не может быть использовано так как превышает размер в 3Мб");
                        return;
                    }
                    this.$emit('change', {
                        file: file,
                        value: reader.result
                    });
                };

                reader.readAsDataURL(file)
            }
        },
        computed: {}
    }
</script>

<style lang="scss">
.j-avatar-chooser {

}
</style>