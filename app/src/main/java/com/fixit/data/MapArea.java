package com.fixit.data;

/**
 * Created by Kostyantin on 12/20/2016.
 */

public class MapArea {

    private String id;
    private String parentId;
    private String name;
    private String type;
    private Geometry geometry;

    public MapArea() { }

    public MapArea(String id, String parentId, String name, String type, Geometry geometry) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return "MapArea{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", geometry=" + geometry +
                '}';
    }

}
