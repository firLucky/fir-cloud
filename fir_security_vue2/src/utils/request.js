import axios from 'axios'
import securityUtils from "@/utils/securityUtils";

//引入axios
// 动态获取本机ip，作为连接后台服务的地址,但访问地址不能是localhost
// 为了灵活配置后台地址，后期需要更改为，配置文件指定字段决定优先使用配置ip还是自己生产的ip（如下）
const hostPort = document.location.host;
const hostData = hostPort.split(":")
const host = hostData[0];

//axios.create能创造一个新的axios实例
const server = axios.create({
    baseURL: "http" + "://" + host + ":51002", //配置请求的url
    timeout: 6000, //配置超时时间
    headers: {
        'Content-Type': "application/x-www-form-urlencoded",
    }, //配置请求头

})




/** 请求拦截器 **/
server.interceptors.request.use(function (config) {
    // 非白名单的请求都加上一个请求头

    return securityUtils.gatewayRequest(config);
}, function (error) {
    return Promise.reject(error);
});


/** 响应拦截器 **/
server.interceptors.response.use(function (response) {

    return securityUtils.gatewayResponse(response);
}, function (error) {
    // axios请求服务器端发生错误的处理
    return Promise.reject(error);
});


/**
 * 下载文件
 *
 * @param url 后台请求地址
 * @param method 请求方法（get/post...）
 * @param obj 向后端传递参数数据
 * @returns AxiosPromise 后端接口返回数据
 */
export function downloadInterface(url, method, obj) {
    return server({
        url: url,
        method: method,
        params: obj,
        responseType: 'blob',
    })
}


/**
 * 上传文件
 *
 * @param url 后台请求地址
 * @param method 请求方法（get/post...）
 * @param formData 文件
 * @param obj 向后端传递参数数据
 * @returns AxiosPromise 后端接口返回数据
 */
export function uploadInterface(url, method, formData, obj) {
    return server({
        url: url,
        method: method,
        data: formData,
        params: obj,
        headers: {"Content-Type": "multipart/form-data"},
        timeout: 30000,
    })
}


/**
 * 定义一个函数-用于接口
 * 利用我们封装好的request发送请求
 * @param url 后台请求地址
 * @param method 请求方法（get/post...）
 * @param obj 向后端传递参数数据
 * @returns AxiosPromise 后端接口返回数据
 */
export function dataInterface(url, method, obj) {
    return server({
        url: url,
        method: method,
        params: obj
    })
}


export default server
