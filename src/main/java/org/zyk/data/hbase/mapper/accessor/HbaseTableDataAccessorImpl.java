package org.zyk.data.hbase.mapper.accessor;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.zyk.data.hbase.mapper.model.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseTableDataAccessorImpl implements HbaseTableDataAccessor {
    private String tableName;

    private HbaseClient hbaseClient;

    private Connection connection;

    public static HbaseTableDataAccessor create(String tableName, HbaseClient hbaseClient) throws IOException {
        return new HbaseTableDataAccessorImpl(tableName, hbaseClient);
    }

    private HbaseTableDataAccessorImpl(String tableName, HbaseClient hbaseClient) throws IOException {
        this.tableName = tableName;
        this.hbaseClient = hbaseClient;
        this.connection = hbaseClient.getConnection();
    }

    public void put(Put put) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            table.put(put);
        }
    }

    public void put(List<Put> putList) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            table.put(putList);
        }
    }

    public Result get(byte[] rowkey) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(rowkey);
            return table.get(get);
        }
    }

    public Result get(byte[] rowkey, byte[] family) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(rowkey);
            get.addFamily(family);
            return table.get(get);
        }
    }

    public Result get(byte[] rowkey, byte[] family, byte[] column) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(rowkey);
            get.addColumn(family, column);
            return table.get(get);
        }
    }

    public Result get(byte[] rowkey, List<Tuple2<byte[], byte[]>> columns) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(rowkey);
            for (Tuple2<byte[], byte[]> item : columns) {
                get.addColumn(item.getValue1(), item.getValue2());
            }
            return table.get(get);
        }
    }

    public List<Result> scan(byte[] startRow, byte[] stopRow) throws IOException {
        Scan scan = new Scan();
        if (startRow != null) {
            scan.setStartRow(startRow);
        }

        if (stopRow != null) {
            scan.setStopRow(stopRow);
        }
        scan.setCaching(1000);

        try (Table table = connection.getTable(TableName.valueOf(tableName));
             ResultScanner scanner = table.getScanner(scan)) {
            List<Result> list = new ArrayList<>();
            for (Result result : scanner) {
                list.add(result);
            }
            return list;
        }
    }

    public List<Result> scan(byte[] startRow, byte[] stopRow, byte[] family, List<byte[]> columns) throws IOException {
        Scan scan = new Scan();

        for (byte[] column : columns) {
            scan.addColumn(family, column);
        }

        return scan(startRow, stopRow, scan);
    }

    public List<Result> scan(byte[] startRow, byte[] stopRow, byte[] family) throws IOException {
        Scan scan = new Scan();
        scan.addFamily(family);
        return scan(startRow, stopRow, scan);
    }

    public List<Result> scan(byte[] startRow, byte[] stopRow, List<Tuple2<byte[], byte[]>> columns) throws IOException {
        Scan scan = new Scan();

        for (Tuple2<byte[], byte[]> item : columns) {
            scan.addColumn(item.getValue1(), item.getValue2());
        }

        return scan(startRow, stopRow, scan);
    }

    private List<Result> scan(byte[] startRow, byte[] stopRow, Scan scan) throws IOException {
        if (startRow != null) {
            scan.setStartRow(startRow);
        }

        if (stopRow != null) {
            scan.setStopRow(stopRow);
        }

        scan.setCaching(1000);
        try (Table table = connection.getTable(TableName.valueOf(tableName));
             ResultScanner scanner = table.getScanner(scan)) {
            List<Result> list = new ArrayList<>();
            for (Result result : scanner) {
                list.add(result);
            }
            return list;
        }
    }

    public void delete(byte[] rowkey) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Delete d = new Delete(rowkey);
            table.delete(d);
        }
    }

    public void delete(List<byte[]> rowkeyList) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            List<Delete> deleteList = new ArrayList<>(rowkeyList.size());
            for (byte[] rowkey : rowkeyList) {
                deleteList.add(new Delete(rowkey));
            }

            table.delete(deleteList);
        }
    }

    public boolean exists(byte[] rowkey) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(rowkey);
            return table.exists(get);
        }
    }
}
