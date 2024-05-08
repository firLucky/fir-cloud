package com.fir.seata.config;


/**
 * @author fir
 */
public class CommonException extends RuntimeException {

    protected String errorCode;

    protected String[] errorMessageArguments;


    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }


    public CommonException(String message) {
        super(message);
        this.errorCode = "fail";
        this.errorMessageArguments = new String[]{message};
    }


    public CommonException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "fail";
        this.errorMessageArguments = new String[]{message};
    }
}
