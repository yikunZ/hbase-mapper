package org.zyk.data.hbase.mapper.modol;

import org.zyk.data.hbase.mapper.annotation.Column;
import org.zyk.data.hbase.mapper.annotation.Family;
import org.zyk.data.hbase.mapper.annotation.Rowkey;
import org.zyk.data.hbase.mapper.annotation.Table;

import java.util.Objects;

@Table(name = "test_table")
@Family("boolean")
public class BooleanValueBean {
    @Rowkey
    private String rowkey;

    @Column
    private Boolean value;

    @Column
    private boolean valuePrimitive;

    @Column(name = "namedColumn")
    private Boolean namedColumn;

    @Column(defaultValue = "30")
    private Boolean defaultValueColumn;

    public BooleanValueBean() {
    }

    public BooleanValueBean(String rowkey, Boolean value, boolean valuePrimitive, Boolean namedColumn, Boolean defaultValueColumn) {
        this.rowkey = rowkey;
        this.value = value;
        this.valuePrimitive = valuePrimitive;
        this.namedColumn = namedColumn;
        this.defaultValueColumn = defaultValueColumn;
    }

    public boolean getValuePrimitive() {
        return valuePrimitive;
    }

    public void setValuePrimitive(boolean valuePrimitive) {
        this.valuePrimitive = valuePrimitive;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public Boolean getNamedColumn() {
        return namedColumn;
    }

    public void setNamedColumn(Boolean namedColumn) {
        this.namedColumn = namedColumn;
    }

    public Boolean getDefaultValueColumn() {
        return defaultValueColumn;
    }

    public void setDefaultValueColumn(Boolean defaultValueColumn) {
        this.defaultValueColumn = defaultValueColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BooleanValueBean)) return false;
        BooleanValueBean that = (BooleanValueBean) o;
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
