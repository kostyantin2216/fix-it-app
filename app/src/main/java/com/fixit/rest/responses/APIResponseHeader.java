package com.fixit.rest.responses;

import com.fixit.rest.APIError;

import java.util.List;

/**
 * Created by Kostyantin on 3/20/2017.
 */

public class APIResponseHeader {

    private List<APIError> errors;

    public APIResponseHeader() { }

    public List<APIError> getErrors() {
        return errors;
    }

    public void setErrors(List<APIError> errors) {
        this.errors = errors;
    }

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    @Override
    public String toString() {
        return "APIResponseHeader{" +
                "errors=" + errors +
                '}';
    }
}
