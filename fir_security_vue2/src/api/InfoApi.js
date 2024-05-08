import {dataInterface} from "@/utils/request";

export default {


    /** 系统通信密钥 **/
    getPublicKey(obj) {
        return dataInterface("/k","get", obj)
    },

    /** 系统通信密钥 **/
    cn(obj) {
        return dataInterface("/cn","get", obj)
    },


    /** 系统登陆接口 **/
    login(obj) {
        return dataInterface("/auth/login","get", obj)
    },


    oneGetValue(obj){
        return dataInterface("/api-one/getValue", "get", obj)
    },


    twoGetValue(obj){
        return dataInterface("/api-two/getValue", "get", obj)
    },


    postXssData(obj){
        return dataInterface("/api-one/getValue", "post", obj)
    }
}