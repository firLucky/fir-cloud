package com.fir.gateway.config.exception;


import com.fir.gateway.config.GlobalConfig;
import com.fir.gateway.config.result.AjaxResult;
import com.fir.gateway.config.result.AjaxStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;


/**
 * 自定义异常处理工具
 * 异常时用JSON代替HTML异常信息
 *
 * @author fir
 */
@Slf4j
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {


    /**
     * 网关参数配置
     */
    @Resource
    private GlobalConfig globalConfig;


    public JsonExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, webProperties.getResources(), errorProperties, applicationContext);
        log.info(String.valueOf(errorProperties));
        log.info(String.valueOf(errorAttributes));
    }

    /**
     * 重构方法，设置返回属性格式
     */
    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable errorThrowable = getError(request);
        // 自定义异常默认不打印堆栈异常
        // 决定是否打印堆栈异常
        boolean printStackTrace = globalConfig.isPrintStackTrace();
        if (printStackTrace) {
            errorThrowable.printStackTrace();
        }
        // 打印全局异常
        log.error(errorThrowable.getMessage());

        Class<?> errorClass = errorThrowable.getClass();
        String simpleName = errorClass.getSimpleName();
        AjaxStatus ajaxStatus;
        switch (simpleName) {
            case "CustomException":
                // 处理自定义异常
                CustomException customException = (CustomException) errorThrowable;
                ajaxStatus = customException.getAjaxStatus();
                break;
            case "NotFoundException":
            case "ResponseStatusException":
                // 处理404
                ajaxStatus = AjaxStatus.NULL_API;
                break;
            default:
                // 统一返回一个服务错误描述
                ajaxStatus = AjaxStatus.SERVICE_UNAVAILABLE;
                break;
        }

        AjaxResult result = AjaxResult.error(ajaxStatus);
        return ServerResponse.status(200).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result));
    }
}