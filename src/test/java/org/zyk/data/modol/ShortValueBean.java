package org.zyk.data.modol;

import org.zyk.data.util.annotation.Column;
import org.zyk.data.util.annotation.Family;
import org.zyk.data.util.annotation.Rowkey;
import org.zyk.data.util.annotation.Table;

import java.util.Objects;

@Table(name = "test_table")
@Family("short")
public class ShortValueBean {
    @Rowkey
    private String rowkey;

    @Column
    private Short value;

    @Column
    private short valuePrimitive;

    @Column(name = "namedColumn")
    private Short namedColumn;

    @Column(defaultValue = "30")
    private Short defaultValueColumn;

    public ShortValueBean() {
    }

    public ShortValueBean(String rowkey, Short value, short valuePrimitive, Short namedColumn, Short defaultValueColumn) {
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

    public Short getValue() {
        return value;
    }

    public void setValue(Short value) {
        this.value = value;
    }

    public short getValuePrimitive() {
        return valuePrimitive;
    }

    public void setValuePrimitive(short valuePrimitive) {
        this.valuePrimitive = valuePrimitive;
    }

    public Short getNamedColumn() {
        return namedColumn;
    }

    public void setNamedColumn(Short namedColumn) {
        this.namedColumn = namedColumn;
    }

    public Short getDefaultValueColumn() {
        return defaultValueColumn;
    }

    public void setDefaultValueColumn(Short defaultValueColumn) {
        this.defaultValueColumn = defaultValueColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortValueBean)) return false;
        ShortValueBean that = (ShortValueBean) o;
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
