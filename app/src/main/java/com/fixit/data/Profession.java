package com.fixit.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Kostyantin on 12/23/2016.
 */

public class Profession implements DataModelObject, Parcelable {

    public final static Comparator<Profession> NAME_COMPARATOR = new Comparator<Profession>() {
        @Override
        public int compare(Profession o1, Profession o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private long id;
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isActive;
    private Date updatedAt;

    public Profession() { }

    public Profession(long id, String name, String description, String imageUrl, Boolean isActive, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Profession{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isActive=" + isActive +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeValue(this.isActive);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
    }

    protected Profession(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }

    public static final Creator<Profession> CREATOR = new Creator<Profession>() {
        @Override
        public Profession createFromParcel(Parcel source) {
            return new Profession(source);
        }

        @Override
        public Profession[] newArray(int size) {
            return new Profession[size];
        }
    };
}
