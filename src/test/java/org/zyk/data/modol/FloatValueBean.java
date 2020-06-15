package org.zyk.data.modol;

import org.zyk.data.annotation.Column;
import org.zyk.data.annotation.Family;
import org.zyk.data.annotation.Rowkey;
import org.zyk.data.annotation.Table;

import java.util.Objects;

@Table(name = "test_table")
@Family("float")
public class FloatValueBean {
    @Rowkey
    private String rowkey;

    @Column
    private Float value;

    @Column
    private float valuePrimitive;

    @Column(name = "namedColumn")
    private Float namedColumn;

    @Column(defaultValue = "30")
    private Float defaultValueColumn;

    public FloatValueBean() {
    }

    public FloatValueBean(String rowkey, Float value, float valuePrimitive, Float namedColumn, Float defaultValueColumn) {
        this.rowkey = rowkey;
        this.value = value;
        this.valuePrimitive = valuePrimitive;
        this.namedColumn = namedColumn;
        this.defaultValueColumn = defaultValueColumn;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public float getValuePrimitive() {
        return valuePrimitive;
    }

    public void setValuePrimitive(float valuePrimitive) {
        this.valuePrimitive = valuePrimitive;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getNamedColumn() {
        return namedColumn;
    }

    public void setNamedColumn(Float namedColumn) {
        this.namedColumn = namedColumn;
    }

    public Float getDefaultValueColumn() {
        return defaultValueColumn;
    }

    public void setDefaultValueColumn(Float defaultValueColumn) {
        this.defaultValueColumn = defaultValueColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatValueBean)) return false;
        FloatValueBean that = (FloatValueBean) o;
        return Objects.equals(getRowkey(), that.getRowkey()) &&
                Objects.equals(getValue(), that.getValue()) &&
                Objects.equals(getNamedColumn(), that.getNamedColumn()) &&
                Objects.equals(getDefaultValueColumn(), that.getDefaultValueColumn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRowkey());
    }
}
