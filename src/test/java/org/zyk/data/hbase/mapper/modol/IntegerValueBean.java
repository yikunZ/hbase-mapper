package org.zyk.data.hbase.mapper.modol;

import org.zyk.data.hbase.mapper.annotation.Column;
import org.zyk.data.hbase.mapper.annotation.Family;
import org.zyk.data.hbase.mapper.annotation.Rowkey;
import org.zyk.data.hbase.mapper.annotation.Table;

import java.util.Objects;

@Table(name = "test_table")
@Family("integer")
public class IntegerValueBean {
    @Rowkey
    private String rowkey;

    @Column
    private Integer value;

    @Column
    private int valuePrimitive;

    @Column(name = "namedColumn")
    private Integer namedColumn;

    @Column(defaultValue = "30")
    private Integer defaultValueColumn;

    public IntegerValueBean() {
    }

    public IntegerValueBean(String rowkey, Integer value, int valuePrimitive, Integer namedColumn, Integer defaultValueColumn) {
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public int getValuePrimitive() {
        return valuePrimitive;
    }

    public void setValuePrimitive(int valuePrimitive) {
        this.valuePrimitive = valuePrimitive;
    }

    public Integer getNamedColumn() {
        return namedColumn;
    }

    public void setNamedColumn(Integer namedColumn) {
        this.namedColumn = namedColumn;
    }

    public Integer getDefaultValueColumn() {
        return defaultValueColumn;
    }

    public void setDefaultValueColumn(Integer defaultValueColumn) {
        this.defaultValueColumn = defaultValueColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerValueBean)) return false;
        IntegerValueBean that = (IntegerValueBean) o;
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
