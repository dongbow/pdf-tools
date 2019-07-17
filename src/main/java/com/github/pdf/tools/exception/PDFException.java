package com.github.pdf.tools.exception;

/**
 * @author wangdongbo
 * @since 2019/7/3.
 */
public class PDFException extends BaseException {

    public PDFException() {
        super("PDF异常");
    }

    public PDFException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public PDFException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

    public PDFException(String errorMsg, Exception e) {
        super(errorMsg, e);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

}
