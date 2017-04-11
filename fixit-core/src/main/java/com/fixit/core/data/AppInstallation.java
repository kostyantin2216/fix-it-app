package com.fixit.core.data;

import java.util.Date;

/**
 * Created by konstantin on 3/30/2017.
 */

public class AppInstallation {

    private String id;
    private String playStoreUrl;
    private DeviceInfo device;
    private VersionInfo version;
    private Date createdAt;

    public AppInstallation(String id, String playStoreUrl, DeviceInfo device, VersionInfo version, Date createdAt) {
        this.id = id;
        this.playStoreUrl = playStoreUrl;
        this.device = device;
        this.version = version;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayStoreUrl() {
        return playStoreUrl;
    }

    public void setPlayStoreUrl(String playStoreUrl) {
        this.playStoreUrl = playStoreUrl;
    }

    public DeviceInfo getDevice() {
        return device;
    }

    public void setDevice(DeviceInfo device) {
        this.device = device;
    }

    public VersionInfo getVersion() {
        return version;
    }

    public void setVersion(VersionInfo version) {
        this.version = version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AppInstallation{" +
                "id='" + id + '\'' +
                ", playStoreUrl='" + playStoreUrl + '\'' +
                ", device=" + device +
                ", version=" + version +
                ", createdAt=" + createdAt +
                '}';
    }
}
