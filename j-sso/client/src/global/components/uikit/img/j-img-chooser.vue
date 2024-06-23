<template>
    <div class="j-image-chooser">
        <input :accept="accept"
               hidden
               :disabled="disabled"
               id="icon-button-file"
               type="file"
               @change="setImage"/>
        <label for="icon-button-file">
            <div class="menu" v-if="!disabled">
                <v-icon>edit</v-icon>
            </div>
            <j-img :src="src" :url="url"/>
        </label>
    </div>
</template>

<script>
    export default {
        name: "j-img-chooser",
        props: {
            accept: {
                type: String,
                default: "image/*"
            },
            src: {
                type: String,
                default: null
            },
            url: {
                type: String,
                default: null
            },
            disabled: {
                type: Boolean,
                default: false
            }
        },
        methods: {
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
        }
    }
</script>

<style lang="scss">
.j-image-chooser {
    cursor: pointer;
    position: relative;
    width: 100%;
    height: 100%;

    img {
        object-fit: cover;
        height: 100%;
        width: 100%;
    }

    .menu {
        cursor: pointer;
        position: absolute;
        z-index: 50;
        height: 100%;
        width: 100%;
        opacity: 0;
        visibility: hidden;
        background-color: rgba(0, 0, 0, 0.25);
        transition: opacity .4s ease-out, visibility .4s ease-out;
        display: flex;
        justify-content: center;
        align-items: center;

        svg {
            color: white;
            width: 50px;
            height: 50px;
        }
    }

    &:hover {
        .menu {
            opacity: 1;
            visibility: visible;
        }
    }
}
</style>