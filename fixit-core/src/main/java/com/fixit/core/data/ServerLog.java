package com.fixit.core.data;

import java.util.Date;

/**
 * Created by Kostyantin on 12/24/2016.
 */

public class ServerLog {

    private String userId;
    private String installationId;
    private String level;
    private String tag;
    private String message;
    private String stackTrace;
    private DeviceInfo deviceInfo;
    private VersionInfo versionInfo;
    private Date createdAt;

    public ServerLog() { }

    public ServerLog(String userId, String installationId, String level, String tag, String message, String stackTrace,
                     DeviceInfo deviceInfo, VersionInfo versionInfo, Date createdAt) {
        this.userId = userId;
        this.installationId = installationId;
        this.level = level;
        this.tag = tag;
        this.message = message;
        this.stackTrace = stackTrace;
        this.deviceInfo = deviceInfo;
        this.versionInfo = versionInfo;
        this.createdAt = createdAt;
    }

    public ServerLog(String level, String tag, String message, String stackTrace,
                     DeviceInfo deviceInfo, VersionInfo versionInfo, Date createdAt) {
        this.level = level;
        this.tag = tag;
        this.message = message;
        this.stackTrace = stackTrace;
        this.deviceInfo = deviceInfo;
        this.versionInfo = versionInfo;
        this.createdAt = createdAt;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ServerLog{" +
                "userId='" + userId + '\'' +
                ", installationId='" + installationId + '\'' +
                ", level='" + level + '\'' +
                ", tag='" + tag + '\'' +
                ", message='" + message + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", deviceInfo=" + deviceInfo +
                ", versionInfo=" + versionInfo +
                ", createdAt=" + createdAt +
                '}';
    }
}
