package com.fir.seata.config;

import lombok.Data;
import java.io.Serializable;


@Data
public class Result implements Serializable {
    private Integer code;
    private String msg;
    private Object data;


    public static Result fail(Integer code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result success(Object data) {
        return fail(200,"请求成功",data);
    }

    public static Result error(Object data) {
        return fail(400,"请求失败",data);
    }
}
