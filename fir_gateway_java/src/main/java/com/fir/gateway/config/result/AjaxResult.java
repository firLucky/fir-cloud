package com.fir.gateway.config.result;

import java.io.Serializable;
import java.util.HashMap;


/**
 * 操作消息-JSON
 *
 * @author fir
 */
public class AjaxResult extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";


    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }


    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  状态描述
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }


    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  状态描述
     * @param data 数据对象
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }


    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        AjaxStatus success = AjaxStatus.SUCCESS;
        return AjaxResult.success(success.getCode(), success.getMsg());
    }


    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        AjaxStatus success = AjaxStatus.SUCCESS;
        return AjaxResult.success(success.getMsg(), data);
    }


    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(AjaxStatus success) {

        return AjaxResult.success(success.getMsg(), new HashMap<>(0));
    }


    /**
     * 返回成功消息
     *
     * @param code 状态吗
     * @param msg  状态描述
     * @return 消息体
     */
    public static AjaxResult success(Integer code, String msg) {
        return new AjaxResult(code, msg);
    }


    /**
     * 返回成功消息
     *
     * @param msg  状态描述
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        AjaxStatus success = AjaxStatus.SUCCESS;
        return new AjaxResult(success.getCode(), msg, data);
    }


    /**
     * 返回特定状态描述
     *
     * @param statusCode 特定的枚举结果
     * @param data       数据对象
     * @return 请求结果
     */
    public static AjaxResult success(StatusCode statusCode, Object data) {
        return new AjaxResult(statusCode.getCode(), statusCode.getMsg(), data);
    }


    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static AjaxResult error() {
        return AjaxResult.error(AjaxStatus.LOSE_OPERATION.getMsg());
    }


    /**
     * 返回错误消息
     *
     * @param msg 状态描述
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, new HashMap<>(0));
    }


    /**
     * 返回错误消息
     *
     * @param msg  状态描述
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        AjaxStatus loseEfficacy = AjaxStatus.LOSE_EFFICACY;
        return new AjaxResult(loseEfficacy.getCode(), msg, data);
    }


    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  状态描述
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, new HashMap<>(0));
    }


    /**
     * 返回特定状态描述
     *
     * @param statusCode 特定的枚举结果
     * @param data       数据对象
     * @return 请求结果
     */
    public static AjaxResult error(StatusCode statusCode, Object data) {
        return new AjaxResult(statusCode.getCode(), statusCode.getMsg(), data);
    }


    /**
     * 返回特定状态描述
     *
     * @param statusCode 特定的枚举结果
     * @return 请求结果
     */
    public static AjaxResult error(StatusCode statusCode) {
        return new AjaxResult(statusCode.getCode(), statusCode.getMsg(), new HashMap<>(0));
    }
}
