package org.zyk.data.modol;

import org.zyk.data.util.annotation.Column;
import org.zyk.data.util.annotation.Family;
import org.zyk.data.util.annotation.Rowkey;
import org.zyk.data.util.annotation.Table;

import java.util.Arrays;
import java.util.Objects;

@Table(name = "test_table")
@Family("array")
public class ArrayBean {
    @Rowkey
    private String rowkey;

    @Column
    private Integer[] integerArray;

    public ArrayBean() {
    }

    public ArrayBean(String rowkey, Integer[] integerArray) {
        this.rowkey = rowkey;
        this.integerArray = integerArray;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public Integer[] getIntegerArray() {
        return integerArray;
    }

    public void setIntegerArray(Integer[] integerArray) {
        this.integerArray = integerArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayBean)) return false;
        ArrayBean arrayBean = (ArrayBean) o;
        return Objects.equals(getRowkey(), arrayBean.getRowkey()) &&
                Arrays.equals(getIntegerArray(), arrayBean.getIntegerArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRowkey());
    }
}
