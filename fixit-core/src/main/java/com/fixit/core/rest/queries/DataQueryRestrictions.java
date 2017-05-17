package com.fixit.core.rest.queries;

/**
 * Created by konstantin on 5/17/2017.
 */

public class DataQueryRestrictions {

    /**
     * Apply an "equal" constraint to the named property
     *
     * @param property The name of the property
     * @param value The value to use in comparison
     *
     * @return DataApiQuery
     */
    public static DataApiQuery eq(String property, String value) {
        return new DataApiQuery(property, "=", value);
    }

    public static DataApiQuery eq(String property, Integer value) {
        return new DataApiQuery(property, "=", String.valueOf(value));
    }

    /**
     * Apply a "not equal" constraint to the named property
     *
     * @param property The name of the property
     * @param value The value to use in comparison
     *
     * @return DataApiQuery
     */
    public static DataApiQuery ne(String property, String value) {
        return new DataApiQuery(property, "!=", value);
    }

    /**
     * Apply a "greater than" constraint to the named property
     *
     * @param property The name of the property
     * @param value The value to use in comparison
     *
     * @return DataApiQuery
     */
    public static DataApiQuery gt(String property, String value) {
        return new DataApiQuery(property, ">", value);
    }

    public static DataApiQuery gt(String property, Long value) {
        return new DataApiQuery(property, ">", String.valueOf(value));
    }

    /**
     * Apply a "less than" constraint to the named property
     *
     * @param property The name of the property
     * @param value The value to use in comparison
     *
     * @return DataApiQuery
     */
    public static DataApiQuery lt(String property, String value) {
        return new DataApiQuery(property, "<", value);
    }

    /**
     * Apply a "greater then or equal" constraint to the named property
     *
     * @param property The name of the property
     * @param value The value to use in comparison
     *
     * @return DataApiQuery
     */
    public static DataApiQuery ge(String property, String value) {
        return new DataApiQuery(property, ">=", value);
    }

    public static DataApiQuery ge(String property, Long value) {
        return new DataApiQuery(property, ">=", String.valueOf(value));
    }

    /**
     * Apply a "less then or equal" constraint to the named property
     *
     * @param property The name of the property
     * @param value The value to use in comparison
     *
     * @return DataApiQuery
     */
    public static DataApiQuery le(String property, String value) {
        return new DataApiQuery(property, "<=", value);
    }

}
