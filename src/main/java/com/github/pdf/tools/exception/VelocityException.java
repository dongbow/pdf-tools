package com.github.pdf.tools.exception;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public class VelocityException extends BaseException {

    public VelocityException() {
        super("Velocity异常");
    }

    public VelocityException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public VelocityException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

    public VelocityException(String errorMsg, Exception e) {
        super(errorMsg, e);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }
}
