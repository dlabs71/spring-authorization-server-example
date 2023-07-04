<template>
    <div class="form-wrapper">
        <dl-login/>
        <div class="inner-form">
            <slot name="default"/>
        </div>
    </div>
</template>

<script>
import DlLogin from './j-login';

export default {
    name: "form-wrapper",
    components: {DlLogin}
}
</script>

<style scoped lang="scss">
    .form-wrapper {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 700px;
        position: relative;

        .inner-form {
            border-radius: 10px;
            box-shadow: 0 5px 5px -3px rgba(0, 0, 0, .2), 0 8px 10px 1px rgba(0, 0, 0, .14), 0 3px 14px 2px rgba(0, 0, 0, .12);
            padding: 50px 20px 50px 30px;
            width: 500px;
            height: 700px;
            background-color: white;
            position: relative;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
    }
</style>