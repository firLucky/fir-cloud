package com.fir.gateway.config.result;

import lombok.Getter;


/**
 * 返回值状态与描述
 *
 * @author fir
 */

@Getter
public enum AjaxStatus implements StatusCode {
    /**
     * 请求成功
     */
    SUCCESS(200, "请求成功"),
    /**
     * 登录成功
     */
    SUCCESS_LOGIN(200, "登录成功"),
    /**
     * 登出成功
     */
    SUCCESS_LOGOUT(200, "登出成功"),
    /**
     * 启动成功
     */
    SUCCESS_FLOW_START(200, "启动成功"),
    /**
     * 暂无数据
     */
    NO_DATA(200, "暂无数据"),
    /**
     * 错误请求
     */
    BAD_REQUEST(400, "错误请求"),
    /**
     * 登录过期
     */
    EXPIRATION_TOKEN(401, "登录过期"),
    /**
     * 服务不存在
     */
    FAILED_SERVICE_DOES(404, "服务不存在"),
    /**
     * 账号或密码为空
     */
    NULL_LOGIN_DATA(480, "账号或密码为空"),
    /**
     * 建立通信-通信建立失败
     */
    FAILED_COMMUNICATION(481, "通信建立失败"),
    /**
     * 接口不存在
     */
    NULL_API(404, "接口不存在"),
    /**
     * 建立通信-非法请求
     */
    ILLEGAL_REQUEST(482, "非法请求"),
    /**
     * 请求失败
     */
    LOSE_EFFICACY(490, "请求失败"),
    /**
     * 操作失败
     */
    LOSE_OPERATION(491, "操作失败"),
    /**
     * 请求失败
     */
    FAILED(500, "请求失败"),
    /**
     * 服务不可用(gateway网关总定义-所有未定义处理的异常都返回该异常)
     */
    SERVICE_UNAVAILABLE(500, "服务不可用"),
    /**
     * 请求整体加密-无效会话
     */
    SESSION_INVALID(601, "无效会话"),
    /**
     * 请求整体加密-会话过期
     */
    SESSION_EXPIRE(602, "会话过期"),
    /**
     * 防重放校验失败
     */
    ANTI_REPLAY_VERIFY_FAILED(701, "防重放校验失败"),
    /**
     * 防重放校验失败
     */
    INTEGRITY_VERIFY_FAILED(801, "完整性校验失败"),
    /**
     * 预留
     */
    PASS(1000, "请求失败");

    private final int code;
    private final String msg;

    AjaxStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}