package org.zyk.data.hbase.mapper.mapper;

import org.zyk.data.hbase.mapper.model.TableDefineInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Mapper {
    Map<Class, TableDefineInfo> TABLE_DEFINE_INFO_MAP = new ConcurrentHashMap<>();

    /**
     * 添加数据
     *
     * @param obj 要添加的数据
     */
    void put(Object obj) throws IOException;

    /**
     * 批量添加数据
     *
     * @param list 数据列表
     * @param <T>  添加的数据表定义类型
     */
    <T> void put(List<T> list) throws IOException;

    /**
     * @param rowkey rowkey
     * @param clazz  获取的表定义类型
     * @param <T>    获取结果的类型
     * @return 获取结果
     */
    <T> T get(Object rowkey, Class<T> clazz) throws IOException;

    /**
     * 获取数据
     *
     * @param rowkey            rowkey
     * @param tableName         表名
     * @param clazz             要获取的列数据的类型（需要与columnsWithFamily顺序对应）
     * @param columnsWithFamily 要获取的带有列簇的列名列表（格式如 cf:column）
     * @return 数据
     */
    List get(Object rowkey, String tableName, List<Class<?>> clazz, List<String> columnsWithFamily) throws IOException;

    /**
     * 批量获取数据
     *
     * @param startRow 查询的开始rowkey （包含该rowkey）
     * @param stopRow  查询的结束rowkey（不包含rowkey）
     * @param clazz    表定义类型
     * @return 获取的数据
     */
    <T> List<T> scan(Object startRow, Object stopRow, Class<T> clazz) throws IOException;

    /**
     * 批量获取数据
     *
     * @param startRow          查询的开始rowkey （包含该rowkey）
     * @param stopRow           查询的结束rowkey（不包含rowkey）
     * @param tableName         表名
     * @param clazz             要获取的列数据的类型（需要与columnsWithFamily顺序对应）
     * @param columnsWithFamily 要获取的带有列簇的列名列表（格式如 cf:column）
     * @return 获取的数据
     */
    List<List<Object>> scan(Object startRow, Object stopRow, String tableName
            , List<Class<?>> clazz, List<String> columnsWithFamily) throws IOException;

    /**
     * 删除数据
     *
     * @param rowkey rowkey
     * @param clazz  表定义类型
     */
    void delete(Object rowkey, Class<?> clazz) throws IOException;

    /**
     * 删除数据
     *
     * @param rowkey    rowkey
     * @param tableName 表名
     */
    void delete(Object rowkey, String tableName) throws IOException;

    /**
     * 批量删除数据
     *
     * @param rowkeyList 要删除的rowkey列表
     * @param clazz      表定义类型
     */
    void delete(List<Object> rowkeyList, Class<?> clazz) throws IOException;

    /**
     * 批量删除数据
     *
     * @param rowkeyList 要删除的rowkey列表
     * @param tableName  表定义类型
     */
    void delete(List<Object> rowkeyList, String tableName) throws IOException;

    /**
     * 是否存在数据
     *
     * @param rowkey rowkey
     * @param clazz  表定义类型
     * @return 是否存在
     */
    boolean exists(Object rowkey, Class<?> clazz) throws IOException;

    /**
     * 是否存在数据
     *
     * @param rowkey    rowkey
     * @param tableName 表名
     * @return 是否存在
     */
    boolean exists(Object rowkey, String tableName) throws IOException;

    static TableDefineInfo getTableDefineInfo(Class clazz) {
        TableDefineInfo tableDefineInfo = TABLE_DEFINE_INFO_MAP.get(clazz);
        if (tableDefineInfo != null) {
            return tableDefineInfo;
        }

        TableDefineInfo newTableDefineInfo = new TableDefineInfo(clazz);
        tableDefineInfo = TABLE_DEFINE_INFO_MAP.putIfAbsent(clazz, newTableDefineInfo);
        return tableDefineInfo == null ? newTableDefineInfo : tableDefineInfo;
    }
}
