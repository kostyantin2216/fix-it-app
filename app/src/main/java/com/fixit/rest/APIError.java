package com.fixit.rest;

import java.util.List;

/**
 * Created by Kostyantin on 3/20/2017.
 */

public class APIError {

    public enum Error {

        MISSING_DATA(1),
        INVALID_DATA(2),
        UNSUPPORTED(3),
        UNKNOWN(4);

        public final int code;

        Error(int code) {
            this.code = code;
        }
    }

    private int code;
    private String description;

    public APIError() { }

    public APIError(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static boolean contains(Error error, List<APIError> apiErrors) {
        for(APIError apiError : apiErrors) {
            if(apiError.code == error.code) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "APIError{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }

}
