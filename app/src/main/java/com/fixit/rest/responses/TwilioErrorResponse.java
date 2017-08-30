package com.fixit.rest.responses;

/**
 * Created by konstantin on 5/16/2017.
 */
public class TwilioErrorResponse {
    private int code;
    private int status;
    private String message;
    private String more_info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMore_info() {
        return more_info;
    }

    public void setMore_info(String more_info) {
        this.more_info = more_info;
    }

    @Override
    public String toString() {
        return "TwilioErrorResponse{" +
                "code=" + code +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", more_info='" + more_info + '\'' +
                '}';
    }
}
