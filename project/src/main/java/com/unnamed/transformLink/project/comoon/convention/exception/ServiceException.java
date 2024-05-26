package com.unnamed.transformLink.project.comoon.convention.exception;


import com.unnamed.transformLink.project.comoon.convention.errorcode.IErrorCode;

import java.util.Optional;

import static com.unnamed.transformLink.project.comoon.convention.errorcode.BaseErrorCode.SERVICE_ERROR;


/**
 * 服务端异常
 */
public class ServiceException extends AbstractException {

    public ServiceException(String message) {
        this(message, null, SERVICE_ERROR);
    }

    public ServiceException(IErrorCode errorCode) {
        this(null, errorCode);
    }

    public ServiceException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(Optional.ofNullable(message).orElse(errorCode.message()), throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
