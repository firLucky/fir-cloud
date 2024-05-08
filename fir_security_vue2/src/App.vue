<template>
    <div id="app">
        <div class="version-switch">
            <button @click="switchVersion('/')">登录</button>
            <button @click="switchVersion('/test')">逐步</button>
        </div>
        <router-view></router-view>
    </div>
</template>

<script>

export default {
    name: 'App',
    components: {},

    methods: {
        switchVersion(path) {
            this.$router.push(path);
        },
    }
}
</script>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    /*color: #2c3e50;*/
    min-height: 100vh; /* 最小高度为视口高度，确保垂直居中 */
}
.version-switch {
    width: 100%; /* 宽度设置为100%，独占一整行 */
    height: 2%; /* 宽度设置为100%，独占一整行 */
    margin-top: 20px;
    margin-bottom: 20px;
}

.version-switch button {
    padding: 5px 10px;
    margin-right: 5px;
    justify-content: center; /* 水平居中 */
}
</style>
