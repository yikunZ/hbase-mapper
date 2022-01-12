package org.zyk.data.hbase.mapper.mapper;

import org.zyk.data.hbase.mapper.exception.DataConvertException;

public class DefaultValueConverter {
    public Object convertFromString(String value, Class<?> clazz) {
        String type = clazz.getCanonicalName();
        switch (type) {
            case "java.lang.String":
                return value;

            case "java.lang.Integer":
            case "int":
                return Integer.valueOf(value);

            case "java.lang.Short":
            case "short":
                return Short.valueOf(value);

            case "java.lang.Float":
            case "float":
                return Float.valueOf(value);

            case "java.lang.Double":
            case "double":
                return Double.valueOf(value);

            case "java.lang.Long":
            case "long":
                return Long.valueOf(value);

            case "java.lang.Character":
            case "char":
                return value.charAt(0);
        }

        throw new DataConvertException("unsupported convert type:" + clazz.getCanonicalName());
    }
}
