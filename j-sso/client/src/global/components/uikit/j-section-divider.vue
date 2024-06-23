<template>
    <div class="j-section-divider">
        <span class="divider-line"></span>
        <span class="text">{{ text }}</span>
        <span class="divider-line"></span>
    </div>
</template>

<script>
    export default {
        name: "j-section-divider",
        props: {
            text: {
                type: String,
                default: "или"
            }
        }
    }
</script>

<style scoped lang="scss">
.j-section-divider {
    display: flex;
    align-items: center;
    width: 100%;
    justify-content: center;
    margin: 20px 0;

    span {
        font-style: italic;
        color: #525252;
    }

    .text {
        margin: 0 10px;
    }

    .divider-line {
        height: 1px;
        width: 30%;
        background-color: #525252;
    }

}
</style>