package com.ankki.webapp.common;

import java.io.Serializable;

public class WebResult implements Serializable {

    public static final String CODE_SUCCESS = "000000000";
    public static final String CODE_ERROR = "999999999";

    private String code;
    private String message;
    private Object data;
    private Object logs;

    public WebResult() {}

    public WebResult(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static WebResult success() {
        return new WebResult(CODE_SUCCESS, "操作成功", null);
    }

    public static WebResult success(Object data) {
        return new WebResult(CODE_SUCCESS, "操作成功", data);
    }

    public static WebResult success(String message, Object data) {
        return new WebResult(CODE_SUCCESS, message, data);
    }

    public static WebResult error(String message) {
        return new WebResult(CODE_ERROR, message, null);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public Object getLogs() { return logs; }
    public void setLogs(Object logs) { this.logs = logs; }
}
