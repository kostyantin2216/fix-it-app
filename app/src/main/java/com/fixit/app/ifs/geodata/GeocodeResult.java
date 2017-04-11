package com.fixit.app.ifs.geodata;

import java.util.List;

/**
 * Created by konstantin on 3/30/2017.
 */

public class GeocodeResult {

    private List<AddressComponent> address_components;
    private String formatted_address;
    private GeocodeGeometry geometry;
    private Boolean partial_match;
    private String place_id;
    private List<String> types;

    public List<AddressComponent> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<AddressComponent> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public GeocodeGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GeocodeGeometry geometry) {
        this.geometry = geometry;
    }

    public Boolean getPartial_match() {
        return partial_match;
    }

    public void setPartial_match(Boolean partial_match) {
        this.partial_match = partial_match;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "GeocodeResult{" +
                "address_components=" + address_components +
                ", formatted_address='" + formatted_address + '\'' +
                ", geometry=" + geometry +
                ", partial_match=" + partial_match +
                ", place_id='" + place_id + '\'' +
                ", types=" + types +
                '}';
    }
}
