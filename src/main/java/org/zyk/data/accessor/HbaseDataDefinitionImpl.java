package org.zyk.data.accessor;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HbaseDataDefinitionImpl implements HbaseDataDefinition {

    private HbaseClient hbaseClient;

    public HbaseDataDefinitionImpl(HbaseClient hbaseClient) {
        this.hbaseClient = hbaseClient;
    }

    @Override
    public void createTable(String tableName, String familys) throws IOException {
        createTable(tableName, new byte[][]{Bytes.toBytes(familys)});
    }

    @Override
    public void createTable(String tableName, byte[] familys) throws IOException {
        createTable(tableName, new byte[][]{familys});
    }

    @Override
    public void createTable(String tableName, byte[][] familys) throws IOException {
        try (Admin admin = hbaseClient.getAdmin()) {
            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
            for (byte[] family : familys) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(family);
                hColumnDescriptor.setCompressionType(Compression.Algorithm.SNAPPY);
                hColumnDescriptor.setMaxVersions(1);
                tableDesc.addFamily(hColumnDescriptor);
            }
            admin.createTable(tableDesc);
        }
    }

    @Override
    public void createTable(String tableName, HColumnDescriptor desc) throws IOException {
        try (Admin admin = hbaseClient.getAdmin()) {
            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
            tableDesc.addFamily(desc);
            admin.createTable(tableDesc);
        }
    }

    @Override
    public void dropTable(String tableName) throws IOException {
        try (Admin admin = hbaseClient.getAdmin()) {
            TableName table = TableName.valueOf(tableName);
            admin.disableTable(table);
            admin.deleteTable(table);
        }
    }

    @Override
    public boolean existsTable(String tableName) throws IOException {
        try (Admin admin = hbaseClient.getAdmin()) {
            TableName table = TableName.valueOf(tableName);
            return admin.tableExists(table);
        }
    }

    @Override
    public boolean existsFamily(String tableName, byte[] family) throws IOException {
        try (Admin admin = hbaseClient.getAdmin()) {
            TableName table = TableName.valueOf(tableName);
            HTableDescriptor tableDescriptor = admin.getTableDescriptor(table);
            return tableDescriptor.getFamiliesKeys().contains(family);
        }
    }

    @Override
    public void addFamily(String tableName, byte[] family) throws IOException {
        try (Admin admin = hbaseClient.getAdmin()) {
            TableName table = TableName.valueOf(tableName);
            HTableDescriptor tableDescriptor = admin.getTableDescriptor(table);
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(family);
            tableDescriptor.addFamily(hColumnDescriptor);
            admin.modifyTable(table, tableDescriptor);
        }
    }
}
