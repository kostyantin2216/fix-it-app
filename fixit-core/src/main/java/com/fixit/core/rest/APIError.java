package com.fixit.core.rest;

/**
 * Created by Kostyantin on 3/20/2017.
 */

public class APIError {
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

    @Override
    public String toString() {
        return "APIError{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }

}
