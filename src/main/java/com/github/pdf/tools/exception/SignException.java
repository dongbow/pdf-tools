package com.github.pdf.tools.exception;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public class SignException extends BaseException {

    public SignException() {
        super("签章异常");
    }

    public SignException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public SignException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

    public SignException(String errorMsg, Exception e) {
        super(errorMsg, e);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

}
