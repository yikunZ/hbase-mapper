package org.zyk.data.util;

public class CommonUtil {

    public static String getSetterNameByFieldName(String setterName) {
        return "set" + setterName.substring(0, 1).toUpperCase() + setterName.substring(1);
    }

    public static String getGetterNameByFieldName(String getterName) {
        return "get" + getterName.substring(0, 1).toUpperCase() + getterName.substring(1);
    }
}
