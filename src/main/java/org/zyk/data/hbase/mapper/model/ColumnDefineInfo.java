package org.zyk.data.hbase.mapper.model;

import net.sf.cglib.reflect.FastMethod;
import org.zyk.data.hbase.mapper.annotation.Column;
import org.zyk.data.hbase.mapper.annotation.Family;
import org.zyk.data.hbase.mapper.annotation.Rowkey;
import org.zyk.data.hbase.mapper.util.CommonUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 列定义信息
 */
public class ColumnDefineInfo {
    private TableDefineInfo tableInfo;

    private Class<?> clazz;

    private String fieldName;

    private String columnName;

    private Method getMethod;

    private FastMethod getFastMethod;

    private Method setMethod;

    private FastMethod setFastMethod;

    private Column column;

    private Family family;

    private Rowkey rowkey;

    private String familyName;

    private String defaultValue;

    public ColumnDefineInfo(TableDefineInfo tableInfo, Field field) {
        Class<?> clazz = field.getType();
        this.tableInfo = tableInfo;
        this.clazz = field.getType();
        this.fieldName = field.getName();
        this.column = field.getAnnotation(Column.class);
        this.rowkey = field.getAnnotation(Rowkey.class);

        if (this.column != null) {
            this.columnName = "".equals(this.column.name()) ? this.fieldName : this.column.name();
            this.defaultValue = this.column.defaultValue();
            Family family = field.getAnnotation(Family.class);
            this.family = family == null ? tableInfo.getFamilyAnnotation() : family;
            this.familyName = this.family.value();
        } else if (this.rowkey != null) {
            this.columnName = "rowkey";
        } else {
            throw new RuntimeException("field must has column or rowkey annotation.");
        }

        try {
            String getterName = CommonUtil.getGetterNameByFieldName(fieldName);
            this.getMethod = tableInfo.getClazz().getMethod(getterName);
            this.getFastMethod = tableInfo.getFastClass().getMethod(getMethod);

            String setterName = CommonUtil.getSetterNameByFieldName(fieldName);
            this.setMethod = tableInfo.getClazz().getMethod(setterName, clazz);
            this.setFastMethod = tableInfo.getFastClass().getMethod(setMethod);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getFamilyName() {
        return familyName;
    }

    public Rowkey getRowkey() {
        return rowkey;
    }

    public Family getFamily() {
        return family;
    }

    public TableDefineInfo getTableInfo() {
        return tableInfo;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public FastMethod getGetFastMethod() {
        return getFastMethod;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public FastMethod getSetFastMethod() {
        return setFastMethod;
    }

    public Column getColumn() {
        return column;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
