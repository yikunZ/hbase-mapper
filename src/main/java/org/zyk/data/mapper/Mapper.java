package org.zyk.data.mapper;

import org.apache.hadoop.hbase.TableName;
import org.zyk.data.accessor.HbaseClient;
import org.zyk.data.accessor.HbaseTableDataAccessor;
import org.zyk.data.accessor.HbaseTableDataAccessorImpl;
import org.zyk.data.model.InnerTableName;
import org.zyk.data.model.TableDefineInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Mapper {

    Map<InnerTableName, HbaseTableDataAccessor> ALL_HBASE_DATA_ACCESSORS = new ConcurrentHashMap<>();

    Map<Class, TableDefineInfo> ALL_TABLES = new ConcurrentHashMap<>();

    /**
     * 添加数据
     *
     * @param obj 要添加的数据
     * @throws IOException
     */
    void put(Object obj) throws IOException;

    /**
     * 批量添加数据
     *
     * @param list 数据列表
     * @param <T>  添加的数据表定义类型
     * @throws IOException
     */
    <T> void put(List<T> list) throws IOException;

    /**
     * @param rowkey rowkey
     * @param clazz  获取的表定义类型
     * @param <T>    获取结果的类型
     * @return 获取结果
     * @throws IOException
     */
    <T> T get(Object rowkey, Class<T> clazz) throws IOException;

    /**
     * 获取数据
     *
     * @param rowkey            rowkey
     * @param tableName         表名
     * @param clazz             要获取的列数据的类型（需要与columnsWithFamily顺序对应）
     * @param columnsWithFamily 要获取的带有列簇的列名列表（格式如 cf:column1）
     * @return
     * @throws IOException
     */
    List get(Object rowkey, String tableName, List<Class<?>> clazz, List<String> columnsWithFamily) throws IOException;

    /**
     * 批量获取数据
     *
     * @param startRow 查询的开始rowkey （包含该rowkey）
     * @param stopRow  查询的结束rowkey（不包含rowkey）
     * @param clazz    表定义类型
     * @return 获取的数据
     * @throws IOException
     */
    <T> List<T> scan(Object startRow, Object stopRow, Class<T> clazz) throws IOException;

    /**
     * 批量获取数据
     *
     * @param startRow          查询的开始rowkey （包含该rowkey）
     * @param stopRow           查询的结束rowkey（不包含rowkey）
     * @param tableName         表名
     * @param clazz             要获取的列数据的类型（需要与columnsWithFamily顺序对应）
     * @param columnsWithFamily 要获取的带有列簇的列名列表（格式如 cf:column1）
     * @return 获取的数据
     * @throws IOException
     */
    List<List<Object>> scan(Object startRow, Object stopRow, String tableName, List<Class<?>> clazz, List<String> columnsWithFamily) throws IOException;

    /**
     * 删除数据
     *
     * @param rowkey rowkey
     * @param clazz  表定义类型
     * @throws IOException
     */
    void delete(Object rowkey, Class<?> clazz) throws IOException;

    /**
     * 批量删除数据
     *
     * @param rowkeyList 要删除的rowkey列表
     * @param clazz      表定义类型
     * @throws IOException
     */
    void delete(List<Object> rowkeyList, Class<?> clazz) throws IOException;

    /**
     * 是否存在数据
     *
     * @param rowkey rowkey
     * @param clazz  表定义类型
     * @return 是否存在
     * @throws IOException
     */
    boolean exists(Object rowkey, Class<?> clazz) throws IOException;

    static TableDefineInfo getTableDefineInfo(Class clazz) {
        // 从缓存中获取（双重锁定校验）
        if (ALL_TABLES.containsKey(clazz)) {
            return ALL_TABLES.get(clazz);
        }

        synchronized (ALL_TABLES) {
            if (ALL_TABLES.containsKey(clazz)) {
                return ALL_TABLES.get(clazz);
            }

            TableDefineInfo tableDefineInfo = new TableDefineInfo(clazz);
            ALL_TABLES.put(clazz, tableDefineInfo);
            return tableDefineInfo;
        }
    }

    static HbaseTableDataAccessor getHbaseDataAccessor(String tableName, HbaseClient hbaseClient) throws IOException {
        // 先从缓存中获取dataAccessor，否则创建一个
        TableName table = TableName.valueOf(tableName);
        InnerTableName innerTableName = InnerTableName.create(table);
        if (ALL_HBASE_DATA_ACCESSORS.containsKey(innerTableName)) {
            return ALL_HBASE_DATA_ACCESSORS.get(innerTableName);
        }

        synchronized (ALL_HBASE_DATA_ACCESSORS) {
            if (ALL_HBASE_DATA_ACCESSORS.containsKey(innerTableName)) {
                return ALL_HBASE_DATA_ACCESSORS.get(innerTableName);
            }

            HbaseTableDataAccessor hbaseTableDataAccessor = HbaseTableDataAccessorImpl.create(table, hbaseClient);
            ALL_HBASE_DATA_ACCESSORS.put(innerTableName, hbaseTableDataAccessor);
            return hbaseTableDataAccessor;
        }
    }
}
