package com.fir.gateway.config.exception;


import com.fir.gateway.config.result.AjaxStatus;

/**
 * 自定义通用异常
 * 抛出异常->全局异常捕捉->返回前端
 *
 * @author fir
 */
public class CustomException extends RuntimeException {

    /**
     * code状态码
     */
    private final int code;

    /**
     * 错误状态码
     */
    private final AjaxStatus ajaxStatus;


    public CustomException(AjaxStatus ajaxStatus) {
        super(ajaxStatus.getMsg());
        this.code = ajaxStatus.getCode();
        this.ajaxStatus = ajaxStatus;
    }

    public int getCode() {
        return code;
    }

    public AjaxStatus getAjaxStatus() {
        return ajaxStatus;
    }
}
