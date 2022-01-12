package org.zyk.data.hbase.mapper.modol;

import org.zyk.data.hbase.mapper.annotation.Column;
import org.zyk.data.hbase.mapper.annotation.Family;
import org.zyk.data.hbase.mapper.annotation.Rowkey;
import org.zyk.data.hbase.mapper.annotation.Table;

import java.util.Objects;

@Table(name = "test_table")
@Family("string")
public class StringValueBean {
    @Rowkey
    private String rowkey;

    @Column
    private String value;

    @Column(name = "namedColumn")
    private String namedColumn;

    @Column(defaultValue = "30")
    private String defaultValueColumn;

    public StringValueBean() {
    }

    public StringValueBean(String rowkey, String value, String namedColumn, String defaultValueColumn) {
        this.rowkey = rowkey;
        this.value = value;
        this.namedColumn = namedColumn;
        this.defaultValueColumn = defaultValueColumn;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNamedColumn() {
        return namedColumn;
    }

    public void setNamedColumn(String namedColumn) {
        this.namedColumn = namedColumn;
    }

    public String getDefaultValueColumn() {
        return defaultValueColumn;
    }

    public void setDefaultValueColumn(String defaultValueColumn) {
        this.defaultValueColumn = defaultValueColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringValueBean)) return false;
        StringValueBean that = (StringValueBean) o;
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
