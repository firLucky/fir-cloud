<template>
    <div class="message-box">
        <div class="message-box__header">{{ message }}</div>
        <div class="message-box__body">{{ description }}</div>
        <button class="message-box__close" @click="close">X</button>
    </div>
</template>

<script>
export default {
    name: "MessageBox",
    props: {
        message: { type: String, default: '' },
        duration: { type: Number, default: 1.5 },
        description: { type: String, default: '' },
    },
    methods: {
        close() {
            this.$emit('close')
        },
    },
    mounted() {
        setTimeout(() => {
            this.close()
        }, this.duration * 1000)
    },
}
</script>

<style scoped>
.message-box {
    position: fixed;
    top: 1%;
    left: 50%;
    transform: translateX(-50%);
    z-index: 9999;
    width: 300px;
    height: 20px;
    padding: 20px;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.message-box__header {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 10px;
}

.message-box__body {
    font-size: 14px;
    line-height: 1.5;
    margin-bottom: 20px;
}

.message-box__close {
    position: absolute;
    top: 10px;
    right: 10px;
    width: 20px;
    height: 20px;
    padding: 0;
    border: none;
    background-color: transparent;
    cursor: pointer;
    font-size: 16px;
    font-weight: bold;
    color: #666;
}
</style>