package com.fixit.core.rest.requests;

/**
 * Created by Kostyantin on 3/20/2017.
 */

public class APIRequest<RD> {

    private APIRequestHeader header;
    private RD data;

    public APIRequest() { }

    public APIRequest(APIRequestHeader header, RD data) {
        this.header = header;
        this.data = data;
    }

    public APIRequestHeader getHeader() {
        return header;
    }

    public void setHeader(APIRequestHeader header) {
        this.header = header;
    }

    public RD getData() {
        return data;
    }

    public void setData(RD data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "APIRequest{" +
                "mHeader=" + header +
                '}';
    }
}
