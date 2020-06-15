package org.zyk.data.model;

import org.apache.hadoop.hbase.TableName;

import java.util.Objects;

public class InnerTableName {
    private TableName tableName;

    public static InnerTableName create(TableName tableName) {
        return new InnerTableName(tableName);
    }

    private InnerTableName(TableName tableName) {
        this.tableName = tableName;
    }

    public TableName getTableName() {
        return tableName;
    }

    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        InnerTableName that = (InnerTableName) o;
        return Objects.equals(tableName.getName(), that.tableName.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName.getName());
    }
}
