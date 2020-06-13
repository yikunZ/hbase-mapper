package org.zyk.data.modol;

import org.zyk.data.util.annotation.Column;
import org.zyk.data.util.annotation.Family;
import org.zyk.data.util.annotation.Rowkey;
import org.zyk.data.util.annotation.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Table(name = "test_table")
@Family("array")
public class ListBean {
    @Rowkey
    private String rowkey;

    @Column
    private List<Integer> integerArray;

    public ListBean() {
    }

    public ListBean(String rowkey, List<Integer> integerArray) {
        this.rowkey = rowkey;
        this.integerArray = integerArray;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public List<Integer> getIntegerArray() {
        return integerArray;
    }

    public void setIntegerArray(List<Integer> integerArray) {
        this.integerArray = integerArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListBean)) return false;
        ListBean arrayBean = (ListBean) o;
        return Objects.equals(getRowkey(), arrayBean.getRowkey()) &&
                Objects.equals(getIntegerArray(), arrayBean.getIntegerArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRowkey());
    }
}
