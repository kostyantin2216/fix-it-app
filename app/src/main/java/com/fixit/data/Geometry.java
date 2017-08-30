package com.fixit.data;

import java.util.Arrays;

/**
 * Created by Kostyantin on 12/20/2016.
 */

public class Geometry {

    private String type;
    private double[][][] coordinates;

    public Geometry() { }

    public Geometry(String type, double[][][] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[][][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][][] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "type='" + type + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

}
