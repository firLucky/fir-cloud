<template>
    <div>
        <div>
            <div class="login-form">
                <div>
                    <div>
                        <label>用户名:&nbsp;</label>
                        <input type="text" v-model="login.username">
                    </div>
                    <div>
                        <label >密&nbsp;&nbsp;&nbsp;码:&nbsp;</label>
                        <input type="text" v-model="login.password">
                    </div>
                </div>
            </div>
            <div>
                <button @click="loginApi">用户登录</button>
            </div>
            <div>
                <div class="input-container2">
                    <textarea class="my-input" v-model="token"/>
                </div>
            </div>
        </div>


        <div class="my-container">
            <button class="my-button" @click="oneValue">微服务一测试接口</button>
            <div>
                <textarea class="my-input" v-model="microserviceOneJsonFormData"></textarea>
            </div>
            <div>
                <textarea class="my-input" v-model="microserviceOneJsonData"></textarea>
            </div>
        </div>


        <div class="my-container">
            <button class="my-button" @click="twoValue">微服务二测试接口</button>
            <div>
                <textarea class="my-input" v-model="microserviceTwoJsonData"></textarea>
            </div>
        </div>

    </div>
</template>

<script>
import securityUtils from "@/utils/securityUtils";

export default {
    name: "loginTest",
    data() {
        return {

            token: "",
            login: {
                username: "fir",
                password: "123",
            },

            // 微服务节点一
            microserviceOneJsonFormData: {
                "msg": "普通的客户端消息",
            },
            microserviceOneJsonData: {},


            // 微服务节点二
            microserviceTwoJsonData: {},
        };
    },

    created() {
        this.jsonString();

        this.connection()
    },

    methods: {


        jsonString() {
            // 将JSON数据转换为字符串
            this.microserviceTwoJsonData = JSON.stringify(this.microserviceTwoJsonData, null, 2);
            this.microserviceOneJsonData = JSON.stringify(this.microserviceOneJsonData, null, 2);
            this.microserviceOneJsonFormData = JSON.stringify(this.microserviceOneJsonFormData, null, 2);

        },

        /**
         * 获取与后端建立通信的必备信息
         */
        loginApi() {
            this.$http.login(this.login).then(res => {
                let code = res.code
                let msg = res.msg
                let data = res.data
                securityUtils.set("token", data.token)
                this.token = securityUtils.get("token")
                if (code === 200) {
                    this.$message({message: msg, duration: 1.5, description: ''})
                } else {
                    this.$message({message: "错误", duration: 1.5, description: ''})
                }
            })
        },


        /** 微服务-1 **/
        oneValue() {
            // 在这里可以编写按钮被点击时需要执行的代码

            let data = JSON.parse(this.microserviceOneJsonFormData);

            this.$http.oneGetValue(data).then(res => {
                let msg = res.msg

                let replaceAfter = res;
                replaceAfter = JSON.stringify(replaceAfter, null, 2);
                this.microserviceOneJsonData = replaceAfter;
                this.$message({message: msg, duration: 1.5, description: ''})

            })
        },

        /** 微服务-2 **/
        twoValue() {
            this.$http.twoGetValue().then(res => {
                let msg = res.msg

                let replaceAfter = res
                replaceAfter = JSON.stringify(replaceAfter, null, 2);
                this.microserviceTwoJsonData = replaceAfter;

                this.$message({message: msg, duration: 1.5, description: ''})

            })
        },

        // 与后端建立联系
        async connection() {
            // 获取后端RSA加密公钥
            await this.$http.getPublicKey().then(res => {
                let data = res.data
                securityUtils.dealValidationMessage(data)
            });

            // 获取与后端建立通信的必备信息

            let data = securityUtils.secureConnectionPrepare()
            await this.$http.cn(data).then(res => {
                let data = res.data
                securityUtils.secureConnection(data)
            })
        }
    },
}
</script>

<style scoped>


/* 水平布局，且向左浮动 */
.login-form {
    margin-top: 40px;
}

.login-input {
    display: flex;
    flex-direction: row;
}

.login-form > div {
    float: left;
    margin-right: 10px;
}


/* 全局安全痛惜参数-start */
.my-container {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;

    .input-container > label {
        flex: 1;
    }
}

.my-container > button {
    align-self: flex-start;
}

.my-container > .input-container {
    display: flex;
    flex-direction: column;
    align-items: center;
}


.my-input {
    width: 300px; /* 设置输入框的宽度 */
    height: 200px; /* 设置输入框的高度 */
    border: 1px solid #ccc; /* 设置输入框的边框 */
    border-radius: 5px; /* 设置输入框的圆角 */
    padding: 5px; /* 设置输入框的内边距 */
    font-size: 14px; /* 设置输入框的字体大小 */
    color: white; /* 设置输入框的字体颜色为白色 */
    background-color: #434554; /* 设置输入框的背景色 */
    resize: vertical; /* 设置输入框垂直方向可自适应 */
    float: left; /* 将两个元素浮动在左侧 */
    box-sizing: border-box; /* 元素的内边距和边框不会增加元素的宽度 */
}

.my-button {
    float: left; /* 将两个元素浮动在左侧 */
    box-sizing: border-box; /* 元素的内边距和边框不会增加元素的宽度 */
}
</style>