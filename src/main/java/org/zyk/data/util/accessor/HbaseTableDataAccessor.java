package org.zyk.data.util.accessor;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.zyk.data.util.model.Tuple2;

import java.io.IOException;
import java.util.List;

/**
 * hbase关联到表的数据存取相关操作
 */
public interface HbaseTableDataAccessor {
    void put(Put t) throws IOException;

    void put(List<Put> list) throws IOException;

    Result get(byte[] rowkey) throws IOException;

    Result get(byte[] rowkey, byte[] family) throws IOException;

    Result get(byte[] rowkey, byte[] family, byte[] column) throws IOException;

    Result get(byte[] rowkey, List<Tuple2<byte[], byte[]>> columns) throws IOException;

    List<Result> scan(byte[] startRow, byte[] stopRow) throws IOException;

    List<Result> scan(byte[] startRow, byte[] stopRow, byte[] family) throws IOException;

    List<Result> scan(byte[] startRow, byte[] stopRow, List<Tuple2<byte[], byte[]>> columns) throws IOException;

    void delete(byte[] rowkey) throws IOException;

    void delete(List<byte[]> rowkeyList) throws IOException;

    boolean exists(byte[] rowkey) throws IOException;
}
