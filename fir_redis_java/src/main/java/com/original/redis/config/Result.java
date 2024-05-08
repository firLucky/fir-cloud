package com.original.redis.config;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;


@Data

public class Result implements Serializable {
    private Integer code;  //success 200  &&  fail 400
    private String msg;
    private Object data;

    public static Result fail(String msg) {
        return fail(400, msg, new HashMap<>());
    }

    public static Result fail(Integer code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result success(Object data) {
        return success(200,"请求成功",data);
    }

    public static Result success() {
        return success(200,"请求成功", new HashMap<>());
    }

    public static Result success(Integer code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
