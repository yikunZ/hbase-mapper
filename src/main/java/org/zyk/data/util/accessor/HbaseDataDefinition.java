package org.zyk.data.util.accessor;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.exceptions.DeserializationException;

import java.io.IOException;

/**
 * hbase数据定义（例如针对表的创建、修改删除等修改数据定义）
 */
public interface HbaseDataDefinition {
    void createTable(String tableName, String familys) throws IOException, DeserializationException;

    void createTable(String tableName, byte[] familys) throws IOException, DeserializationException;

    void createTable(String tableName, byte[][] familys) throws IOException, DeserializationException;

    void createTable(String tableName, HColumnDescriptor desc) throws IOException;

    void dropTable(String tableName) throws IOException;

    boolean existsTable(String tableName) throws IOException;

    boolean existsFamily(String tableName, byte[] family) throws IOException;

    void addFamily(String tableName, byte[] family) throws IOException;
}
