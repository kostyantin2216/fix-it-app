package com.fixit.utils;

/**
 * Created by Kostyantin on 4/16/2017.
 */

public class ErrorUtils {

    public final static IllegalArgumentException missingArguments(MissingValuesArgument argument) {
        StringBuilder msg = new StringBuilder();
        msg.append(argument.inClass.getName()).append(" cannot be instantiated without arguments:\n");

        for(MissingValue missingValue : argument.missingValues) {
            msg.append(missingValue.key).append(" of type ").append(missingValue.typeName).append("\n");
        }

        return new IllegalArgumentException(msg.toString());
    }

    private static class MissingValue {
        final String key;
        final String typeName;

        MissingValue(String key, String typeName) {
            this.key = key;
            this.typeName = typeName;
        }
    }

    public static class MissingValuesArgument {
        final Class<?> inClass;
        final MissingValue[] missingValues;
        int position = 0;

        public MissingValuesArgument(Class<?> inClass, int howManyMissing) {
            this.inClass = inClass;
            this.missingValues = new MissingValue[howManyMissing];
        }

        public MissingValuesArgument add(String key, Class<?> type) throws IndexOutOfBoundsException {
            if(this.position < this.missingValues.length) {
                throw new IndexOutOfBoundsException("Too many values, please double check second value in constructor when instantiating this instance.");
            }
            this.missingValues[position++] = new MissingValue(key, type.getName());
            return this;
        }
    }

}
