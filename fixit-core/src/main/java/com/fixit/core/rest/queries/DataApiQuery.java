package com.fixit.core.rest.queries;

/**
 * Created by konstantin on 5/17/2017.
 */

public class DataApiQuery {

    private String prop;
    private String op;
    private String val;

    public DataApiQuery() { }

    public DataApiQuery(String prop, String op, String val) {
        this.prop = prop;
        this.op = op;
        this.val = val;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return prop + " " + op + " " + val;
    }

}
