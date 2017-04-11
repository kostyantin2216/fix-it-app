package com.fixit.core.rest.responses;

/**
 * Created by Kostyantin on 3/20/2017.
 */

public class APIResponse<RD> {

    private APIResponseHeader header;
    private RD data;

    public APIResponse() { }

    public APIResponseHeader getHeader() {
        return header;
    }

    public void setHeader(APIResponseHeader header) {
        this.header = header;
    }

    public RD getData() {
        return data;
    }

    public void setData(RD responseData) {
        this.data = responseData;
    }

    @Override
    public String toString() {
        return "APIResponse{" +
                "header=" + header +
                ", data=" + data +
                '}';
    }
}
