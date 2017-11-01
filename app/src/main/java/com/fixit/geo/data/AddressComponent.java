package com.fixit.geo.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 3/30/2017.
 */

public class AddressComponent {

    public enum Type {
        street_number,
        route,
        sublocality,
        locality,
        administrative_area_level_1,
        country,
        postal_code;

        public static Type find(String name) {
            switch (name) {
                case "street_number":
                    return street_number;
                case "route":
                    return route;
                case "sublocality":
                    return sublocality;
                case "locality":
                    return locality;
                case "administrative_area_level_1":
                    return administrative_area_level_1;
                case "country":
                    return country;
                case "postal_code":
                    return postal_code;
                default:
                    return null;
            }
        }
    }

    private String long_name;
    private String short_name;
    private List<String> types;

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public static Map<Type, AddressComponent> sortByType(List<AddressComponent> addressComponents) {
        Map<Type, AddressComponent> sorted = new HashMap<>();
        for (AddressComponent component : addressComponents) {
            for (String componentType : component.getTypes()) {
                Type type = Type.find(componentType);
                if (type != null) {
                    sorted.put(type, component);
                }
            }
        }
        return sorted;
    }
    @Override
    public String toString() {
        return "AddressComponent{" +
                "long_name='" + long_name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", types=" + types +
                '}';
    }
}
