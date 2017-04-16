package com.sora.zero.tgfc.data.api.model.wrapper;

/**
 * Created by zsy on 3/21/17.
 */

public class BaseWrapper {
    public static final int ERROR_TYPE_NO_ERRORS = -1;

    public static final int ERROR_TYPE_NETWORK_FAIL = 1;
    public static final int ERROR_TYPE_API_ERROR = 2;
    public static final int ERROR_TYPE_LOGIN_REQUIRED = 3;
    public static final int ERROR_TYPE_NOT_AUTHORIZED = 4;
    public static final int ERROR_TYPE_ARGUMENT_ERROR = 5;

    public static final int ERROR_TYPE_NOT_IMAGE = 6;
    public static final int ERROR_TYPE_WRITE_CACHE_FILE_FAIL = 7;
    public static final int ERROR_TYPE_IMAGE_OFF = 8;

    public static final int ERROR_TYPE_OTHERS = 1000;


    public boolean hasError;
    public String errorInfo;
    public int errorType;


    public void setErrorInfo(String errorInfo, int errorType)
    {
        this.hasError = true;
        this.errorInfo = errorInfo;
        this.errorType = errorType;
    }
}
