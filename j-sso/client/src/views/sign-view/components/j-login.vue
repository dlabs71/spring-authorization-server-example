<template>
    <div class="j-login">
        <div class="circle">
            <h1>ID</h1>
        </div>
    </div>
</template>

<script>
export default {
    name: "j-login"
}
</script>

<style lang="scss" scoped>
    @mixin margin-auto {
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        margin: auto;
    }
    
    .j-login {
        cursor: pointer;
        position: absolute;
        width: 100px;
        height: 100px;
        border-radius: 30px;
        display: inline-block;
        top: 0;
        transition: all 0.2s ease;
        margin-top: -70px;
        background: linear-gradient(to bottom right, #f91b4c 0%, #fa8844 100%);
        color: black;
        z-index: 200;
        transform: rotate(-45deg);
        
        .circle {
            width: 60px;
            height: 60px;
            position: absolute;
            background: #fff;
            z-index: 10;
            border-radius: 50%;
            @include margin-auto;
            display: flex;
            align-items: center;
            justify-content: center;
            transform: rotate(45deg);
        }
    }
</style>