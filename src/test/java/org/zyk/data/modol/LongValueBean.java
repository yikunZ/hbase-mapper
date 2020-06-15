package org.zyk.data.modol;

import org.zyk.data.annotation.Column;
import org.zyk.data.annotation.Family;
import org.zyk.data.annotation.Rowkey;
import org.zyk.data.annotation.Table;

import java.util.Objects;

@Table(name = "test_table")
@Family("long")
public class LongValueBean {
    @Rowkey
    private String rowkey;

    @Column
    private Long value;

    @Column
    private long valuePrimitive;

    @Column(name = "namedColumn")
    private Long namedColumn;

    @Column(defaultValue = "30")
    private Long defaultValueColumn;

    public LongValueBean() {
    }

    public LongValueBean(String rowkey, Long value, long valuePrimitive, Long namedColumn, Long defaultValueColumn) {
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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public long getValuePrimitive() {
        return valuePrimitive;
    }

    public void setValuePrimitive(long valuePrimitive) {
        this.valuePrimitive = valuePrimitive;
    }

    public Long getNamedColumn() {
        return namedColumn;
    }

    public void setNamedColumn(Long namedColumn) {
        this.namedColumn = namedColumn;
    }

    public Long getDefaultValueColumn() {
        return defaultValueColumn;
    }

    public void setDefaultValueColumn(Long defaultValueColumn) {
        this.defaultValueColumn = defaultValueColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongValueBean)) return false;
        LongValueBean that = (LongValueBean) o;
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
