/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.api.error;

/**
 *
 * @author g578689
 */
public class SystemException extends RuntimeException {

    private ErrorCode errorCode;
    private String errorDetail;

    public static SystemException wrap(Throwable exception, ErrorCode errorCode) {
        if (exception instanceof SystemException) {
            SystemException se = (SystemException) exception;
            if (errorCode != null && errorCode != se.getErrorCode()) {
                return new SystemException(errorCode, se.getErrorDetail(), exception);
            }
            return se;
        } else {
            return new SystemException(errorCode, exception.getMessage(), exception);
        }
    }

    public static SystemException wrap(Throwable exception) {
        return wrap(exception, null);
    }

    public SystemException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public SystemException(ErrorCode errorCode, String errorDetail) {
        super(errorDetail);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public SystemException(ErrorCode errorCode, String errorDetail, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public SystemException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public SystemException setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

}
