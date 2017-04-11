package com.fixit.core.rest.requests;

/**
 * Created by Kostyantin on 3/20/2017.
 */

public class APIRequestHeader {

    private String userId;
    private String installationId;
    private String latestScreen;

    public APIRequestHeader() { }

    public APIRequestHeader(String userId, String installationId, String latestScreen) {
        this.userId = userId;
        this.installationId = installationId;
        this.latestScreen = latestScreen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getLatestScreen() {
        return latestScreen;
    }

    public void setLatestScreen(String latestScreen) {
        this.latestScreen = latestScreen;
    }

    @Override
    public String toString() {
        return "APIRequestHeader{" +
                "userId='" + userId + '\'' +
                ", installationId='" + installationId + '\'' +
                ", latestScreen='" + latestScreen + '\'' +
                '}';
    }
}
