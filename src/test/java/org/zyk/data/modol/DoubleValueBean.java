package org.zyk.data.modol;

import org.zyk.data.util.annotation.Column;
import org.zyk.data.util.annotation.Family;
import org.zyk.data.util.annotation.Rowkey;
import org.zyk.data.util.annotation.Table;

import java.util.Objects;

@Table(name = "test_table")
@Family("double")
public class DoubleValueBean {
    @Rowkey
    private String rowkey;

    @Column
    private Double value;

    @Column
    private double valuePrimitive;

    @Column(name = "namedColumn")
    private Double namedColumn;

    @Column(defaultValue = "30")
    private Double defaultValueColumn;

    public DoubleValueBean() {
    }

    public DoubleValueBean(String rowkey, Double value, double valuePrimitive, Double namedColumn, Double defaultValueColumn) {
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

    public double getValuePrimitive() {
        return valuePrimitive;
    }

    public void setValuePrimitive(double valuePrimitive) {
        this.valuePrimitive = valuePrimitive;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getNamedColumn() {
        return namedColumn;
    }

    public void setNamedColumn(Double namedColumn) {
        this.namedColumn = namedColumn;
    }

    public Double getDefaultValueColumn() {
        return defaultValueColumn;
    }

    public void setDefaultValueColumn(Double defaultValueColumn) {
        this.defaultValueColumn = defaultValueColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleValueBean)) return false;
        DoubleValueBean that = (DoubleValueBean) o;
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
