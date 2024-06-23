<template>
    <img
        :src="imageUrl"
        class="j-image"
        @error="onError"
        :style="styles"
        :alt="altValue"
    >
</template>

<script>

    import axios from 'axios';

    export default {
        name: "j-img",
        emits: ['errorOnLoad'],
        props: {
            size: {
                type: Number,
                default: 70
            },
            src: {
                type: String,
                default: null
            },
            url: {
                type: String,
                default: null
            },
            alt: {
                type: String,
                default: null
            }
        },
        data() {
            return {
                defaultImg: require('../../../../assets/img/no-image.svg'),
                errorOnLoad: false
            }
        },
        methods: {
            onError() {
                this.errorOnLoad = true;
                this.$emit('errorOnLoad');
            }
        },
        computed: {
            imageUrl() {
                if (this.errorOnLoad) {
                    return this.defaultImg;
                }
                return this.urlValue;
            },
            urlValue() {
                if (!!this.src) {
                    return this.src;
                }
                if (!!this.url) {
                    return axios.defaults.baseURL + this.url;
                }
                return this.defaultImg;
            },
            altValue() {
                if (!!this.alt) {
                    return this.alt;
                }
                return "Изображение";
            },
            styles() {
                return {
                    'width': this.size + "px",
                    'height': this.size + "px"
                }
            }
        },
        watch: {
            urlValue() {
                this.errorOnLoad = false;
            }
        }
    }
</script>

<style scoped lang="scss">
.j-image {
    object-fit: contain;
}
</style>