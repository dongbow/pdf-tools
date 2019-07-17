package com.github.pdf.tools.exception;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public class BaseException extends RuntimeException {
    public int errorCode;
    public String errorMsg;

    public BaseException() {
        super("运行时异常");
    }

    public BaseException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorMsg, Exception e) {
        super(errorMsg, e);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

}
