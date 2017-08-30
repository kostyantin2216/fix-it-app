package com.fixit.general;

/**
 * Created by konstantin on 3/30/2017.
 */

public interface UnexpectedErrorCallback {
    void onUnexpectedErrorOccurred(String msg, Throwable t);
}
