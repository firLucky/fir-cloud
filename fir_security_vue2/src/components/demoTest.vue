<template>
    <div>
        <div class="my-container">
            <button class="my-button" @click="k">获取公钥</button>
            <div>
                <textarea class="my-input" v-model="publicKey"></textarea>
            </div>
        </div>
        <div>
            <button @click="cn">获取密钥</button>
            <div class="my-container">
                <div class="input-container">
                    <label>SecretKey</label>
                    <textarea class="my-input" v-model="secretKey"/>
                </div>

                <div class="input-container">
                    <label>会话id</label>
                    <textarea class="my-input" v-model="sessionId"/>
                </div>
            </div>
        </div>


        <div class="login-form">

            <button @click="loginApi">用户登录</button>
            <div class="login-input">
                <div>
                    <label for="username">用户名：</label>
                    <input type="text" id="username" v-model="login.username">
                </div>
                <div>
                    <label for="password">密码： </label>
                    <input type="text" id="password" v-model="login.password">
                </div>
            </div>
            <div class="input-container2">
                <label for="token">token</label>
                <textarea class="my-input" v-model="token"/>
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


        <div class="my-container">
            <button class="my-button" @click="postXss()">xss攻击验证</button>
            <div>
                <textarea class="my-input" v-model="xssRep">
                </textarea>
            </div>
            <div>
                <textarea class="my-input" v-model="xssRes"></textarea>
            </div>
        </div>


    </div>
</template>

<script>
import securityUtils from "@/utils/securityUtils";

export default {
    name: 'demoTest',
    props: {
        msg: String
    },

    comments: {},

    data() {
        return {
            token: "",
            publicKey: "",
            secretKey: "",
            sessionId: "",

            login: {
                username: "fir",
                password: "123",
            },


            // 微服务节点一
            microserviceOneJsonFormData: {
                "msg":"普通的客户端消息",
            },
            microserviceOneJsonData: {},


            // 微服务节点二
            microserviceTwoJsonData: {},


            // xss对象
            xssRep: "<script>  alert('XSS Attack!');",
            xssRes: ""
        }
    },
    created() {
        this.jsonString();
    },

    methods: {

        jsonString() {
            // 将JSON数据转换为字符串
            this.microserviceTwoJsonData = JSON.stringify(this.microserviceTwoJsonData, null, 2);
            this.microserviceOneJsonData = JSON.stringify(this.microserviceOneJsonData, null, 2);
            this.microserviceOneJsonFormData = JSON.stringify(this.microserviceOneJsonFormData, null, 2);

        },


        /**
         * 获取后端RSA加密公钥
         */
        k() {
            this.$http.getPublicKey().then(res => {
                let code = res.code
                let msg = res.msg
                let data = res.data
                securityUtils.dealValidationMessage(data)
                this.publicKey = securityUtils.get("publicKey")
                if (code === 200) {
                    this.$message({message: msg, duration: 1.5, description: ''})
                } else {
                    this.$message({message: "错误", duration: 1.5, description: ''})
                }
            });
        },


        /**
         * 获取与后端建立通信的必备信息
         */
        cn() {
            let data = securityUtils.secureConnectionPrepare()
            this.$http.cn(data).then(res => {
                let code = res.code
                let msg = res.msg
                let data = res.data
                securityUtils.secureConnection(data)
                this.secretKey = securityUtils.get("secretKey")
                this.sessionId = securityUtils.get("sessionId")
                if (code === 200) {
                    this.$message({message: msg, duration: 1.5, description: ''})
                } else {
                    this.$message({message: "错误", duration: 1.5, description: ''})
                }
            })
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

        /** XSS攻击案例 **/
        postXss() {
            let param = {
                "msg": this.xssRep
            }

            this.$http.postXssData(param).then(res => {
                // let msg = res.msg

                let replaceAfter = res;
                replaceAfter = JSON.stringify(replaceAfter, null, 2);
                this.xssRes = replaceAfter;

                // this.$message({message: msg, duration: 1.5, description: ''})

            })
        }
    }
}
</script>

<style scoped>


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

/* 全局安全痛惜参数-end */


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
