<template>
    <img :src="imageUrl" class="j-image" @error="onError" v-on="$listeners"/>
</template>

<script>

  import axios from 'axios';

  export default {
        name: "j-img",
        props: {
            src: {
                type: String,
                default: null
            },
            fileId: {
                type: Number,
                default: null
            }
        },
        data() {
            return {
                defaultImg: require("@/assets/img/no-image.svg"),
                errorOnLoad: false
            }
        },
        methods: {
            onError() {
                this.$set(this, 'errorOnLoad', true);
            }
        },
        computed: {
            imageUrl() {
                if (this.errorOnLoad) {
                    return this.defaultImg;
                }
                if (!!this.src) {
                    return this.src;
                }
                if (!!this.fileId) {
                    return axios.defaults.bdURL + `/file/download/${this.fileId}?access_token=JWT ${this.$store.getters.getAccessToken}`
                }
                return this.defaultImg;
            }
        }
    }
</script>

<style lang="scss" scoped>
.j-image {
    object-fit: cover;
}
</style>