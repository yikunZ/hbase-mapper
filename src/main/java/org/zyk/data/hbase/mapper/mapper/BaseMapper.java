package org.zyk.data.hbase.mapper.mapper;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.zyk.data.hbase.mapper.accessor.*;
import org.zyk.data.hbase.mapper.annotation.Column;
import org.zyk.data.hbase.mapper.converter.DataWrapper;
import org.zyk.data.hbase.mapper.exception.MapperException;
import org.zyk.data.hbase.mapper.model.ColumnDefineInfo;
import org.zyk.data.hbase.mapper.model.TableDefineInfo;
import org.zyk.data.hbase.mapper.model.Tuple2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class BaseMapper implements Mapper {
    private Map<String, HbaseTableDataAccessor> allHbaseTableDataAccessor = new ConcurrentHashMap<>();

    private HbaseClient hbaseClient;

    /**
     * 默认值转化
     */
    private DefaultValueConverter defaultValueConverter = new DefaultValueConverter();

    private HbaseDataDefinition hbaseDateDefinition;

    private DataWrapper dataWrapper;

    /**
     * 用于检查表定义是否存在
     */
    private Map<String, Set<String>> hasCheckTableExists = new ConcurrentHashMap<>();

    BaseMapper() {
        this.hbaseClient = HbaseClient.create();
        this.dataWrapper = setDataTransform();
        this.hbaseDateDefinition = new HbaseDataDefinitionImpl(hbaseClient);
    }

    BaseMapper(HbaseClient hbaseClient) {
        this.hbaseClient = hbaseClient;
        this.dataWrapper = setDataTransform();
        this.hbaseDateDefinition = new HbaseDataDefinitionImpl(hbaseClient);
    }

    abstract DataWrapper setDataTransform();

    @Override
    public void put(Object obj) throws IOException {
        Class<?> clazz = obj.getClass();
        TableDefineInfo tableDefineInfo = Mapper.getTableDefineInfo(clazz);
        checkOrCreateTable(tableDefineInfo);

        try {
            // 设置rowkey
            Object rowkey = tableDefineInfo.getRowkeyColumn().getGetFastMethod().invoke(obj, new Object[0]);
            byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkey);
            Put put = new Put(rowkeyValue);

            // 设置其他列
            for (ColumnDefineInfo columnDefineInfo : tableDefineInfo.getColumns().values()) {
                Object columnValue = columnDefineInfo.getGetFastMethod().invoke(obj, new Object[0]);
                dataWrapper.wrap(put, columnDefineInfo.getFamilyName(),
                        columnDefineInfo.getColumnName(), columnValue, tableDefineInfo.getNullValueHandle());
            }

            HbaseTableDataAccessor hbaseTableDataAccessor =
                    getHbaseDataAccessor(tableDefineInfo.getTableName(), hbaseClient);
            hbaseTableDataAccessor.put(put);
        } catch (InvocationTargetException ex) {
            throw new MapperException(ex);
        }
    }

    @Override
    public <T> void put(List<T> list) throws IOException {
        if (list == null || list.isEmpty()) {
            return;
        }

        Class<?> clazz = list.get(0).getClass();
        TableDefineInfo tableDefineInfo = Mapper.getTableDefineInfo(clazz);
        checkOrCreateTable(tableDefineInfo);

        try {
            List<Put> putList = new ArrayList<>();
            for (T item : list) {
                // 设置rowkey
                Object rowkey = tableDefineInfo.getRowkeyColumn().getGetFastMethod().invoke(item, new Object[0]);
                byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkey);
                Put put = new Put(rowkeyValue);

                // 设置其他列
                for (ColumnDefineInfo columnDefineInfo : tableDefineInfo.getColumns().values()) {

                    // 获取和设置到put
                    Object columnValue = columnDefineInfo.getGetFastMethod().invoke(item, new Object[0]);
                    dataWrapper.wrap(put, columnDefineInfo.getFamilyName(),
                            columnDefineInfo.getColumnName(), columnValue, tableDefineInfo.getNullValueHandle());
                }

                putList.add(put);
            }

            HbaseTableDataAccessor hbaseTableDataAccessor =
                    getHbaseDataAccessor(tableDefineInfo.getTableName(), hbaseClient);
            hbaseTableDataAccessor.put(putList);
        } catch (InvocationTargetException ex) {
            throw new MapperException(ex);
        }
    }

    @Override
    public <T> T get(Object rowkey, Class<T> clazz) throws IOException {
        TableDefineInfo tableDefineInfo = Mapper.getTableDefineInfo(clazz);
        checkOrCreateTable(tableDefineInfo);

        HbaseTableDataAccessor hbaseTableDataAccessor =
                getHbaseDataAccessor(tableDefineInfo.getTableName(), hbaseClient);

        // 查询数据并获取结果
        List<Tuple2<byte[], byte[]>> columns = tableDefineInfo.getColumns().values().stream().map(o -> {
            byte[] familyName = dataWrapper.convert2Bytes(o.getFamilyName());
            byte[] columnName = dataWrapper.convert2Bytes(o.getColumnName());
            return new Tuple2<>(familyName, columnName);
        }).collect(Collectors.toList());

        byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkey);
        Result result = hbaseTableDataAccessor.get(rowkeyValue, columns);

        return mapResult(tableDefineInfo, result);
    }

    @Override
    public List get(Object rowkey, String tableName, List<Class<?>> classList, List<String> columnsWithFamily) throws IOException {
        if (classList.size() != columnsWithFamily.size()) {
            throw new RuntimeException("Number of elements of classList and columnsWithFamily does not match.");
        }

        // 查询数据并获取结果
        List<Tuple2<byte[], byte[]>> columns = new ArrayList<>();
        for (String column : columnsWithFamily) {
            String[] split = column.split(":");
            if (split.length != 2 || split[0].isEmpty() || split[1].isEmpty()) {
                throw new IllegalArgumentException("columnsWithFamily format must be like family:column.");
            }

            byte[] familyName = dataWrapper.convert2Bytes(split[0]);
            byte[] columnName = dataWrapper.convert2Bytes((split[1]));
            columns.add(new Tuple2<>(familyName, columnName));
        }

        HbaseTableDataAccessor hbaseTableDataAccessor = getHbaseDataAccessor(tableName, hbaseClient);
        byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkey);
        Result result = hbaseTableDataAccessor.get(rowkeyValue, columns);

        ArrayList<Object> returnList = new ArrayList<>(columnsWithFamily.size());
        // 绑定数据
        for (int i = 0; i < columns.size(); i++) {
            Tuple2<byte[], byte[]> column = columns.get(i);

            // 从result中获取数据并转化成field的对应类型
            Object fieldValue = dataWrapper.unwrap(result, column.getValue1(),
                    column.getValue2(), classList.get(i));

            // 赋值
            returnList.add(fieldValue);
        }

        return returnList;
    }

    @Override
    public <T> List<T> scan(Object startRow, Object stopRow, Class<T> clazz) throws IOException {
        TableDefineInfo tableDefineInfo = Mapper.getTableDefineInfo(clazz);
        checkOrCreateTable(tableDefineInfo);

        HbaseTableDataAccessor hbaseTableDataAccessor =
                getHbaseDataAccessor(tableDefineInfo.getTableName(), hbaseClient);

        // 查询数据并获取结果
        List<Tuple2<byte[], byte[]>> columns = tableDefineInfo.getColumns().values().stream().map(o -> {
            byte[] familyName = dataWrapper.convert2Bytes(o.getFamilyName());
            byte[] columnName = dataWrapper.convert2Bytes(o.getColumnName());
            return new Tuple2<>(familyName, columnName);
        }).collect(Collectors.toList());

        byte[] startRowValue = dataWrapper.convert2Bytes(startRow);
        byte[] stopRowValue = dataWrapper.convert2Bytes(stopRow);
        List<Result> resultList = hbaseTableDataAccessor.scan(startRowValue, stopRowValue, columns);

        List<T> returnList = new ArrayList<>();
        for (Result result : resultList) {
            T obj = mapResult(tableDefineInfo, result);
            returnList.add(obj);
        }

        return returnList;
    }

    @Override
    public List<List<Object>> scan(Object startRow, Object stopRow, String tableName
            , List<Class<?>> classList, List<String> columnsWithFamily)
            throws IOException {
        if (classList.size() != columnsWithFamily.size()) {
            throw new IllegalArgumentException("Number of elements of clazzs and columnsWithFamily does not match.");
        }

        // 查询数据并获取结果
        List<Tuple2<byte[], byte[]>> columns = columnsWithFamily.stream().map(o -> {
            String[] split = o.split(":");
            byte[] familyName = dataWrapper.convert2Bytes(split[0]);
            byte[] columnName = dataWrapper.convert2Bytes((split[1]));
            return new Tuple2<>(familyName, columnName);
        }).collect(Collectors.toList());

        HbaseTableDataAccessor hbaseTableDataAccessor = getHbaseDataAccessor(tableName, hbaseClient);
        byte[] startRowValue = dataWrapper.convert2Bytes(startRow);
        byte[] stopRowValue = dataWrapper.convert2Bytes(stopRow);
        List<Result> resultList = hbaseTableDataAccessor.scan(startRowValue, stopRowValue, columns);

        List<List<Object>> returnList = new ArrayList<>(resultList.size());
        // 绑定数据
        for (Result result : resultList) {
            List<Object> returnItem = new ArrayList<>(columns.size());
            for (int i = 0; i < columns.size(); i++) {
                Tuple2<byte[], byte[]> column = columns.get(i);

                // 从result中获取数据并转化成field的对应类型
                Object fieldValue = dataWrapper.unwrap(result, column.getValue1(),
                        column.getValue2(), classList.get(i));

                // 赋值
                returnItem.add(fieldValue);
            }

            returnList.add(returnItem);
        }

        return returnList;
    }

    @Override
    public void delete(Object rowkey, Class<?> clazz) throws IOException {
        TableDefineInfo tableDefineInfo = Mapper.getTableDefineInfo(clazz);
        checkOrCreateTable(tableDefineInfo);

        HbaseTableDataAccessor hbaseTableDataAccessor =
                getHbaseDataAccessor(tableDefineInfo.getTableName(), hbaseClient);

        byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkey);
        hbaseTableDataAccessor.delete(rowkeyValue);
    }

    @Override
    public void delete(List<Object> rowkeyList, Class<?> clazz) throws IOException {
        TableDefineInfo tableDefineInfo = Mapper.getTableDefineInfo(clazz);
        checkOrCreateTable(tableDefineInfo);

        HbaseTableDataAccessor hbaseTableDataAccessor =
                getHbaseDataAccessor(tableDefineInfo.getTableName(), hbaseClient);

        List<byte[]> rowkeyValueList = rowkeyList.stream()
                .map(r -> dataWrapper.convert2Bytes(r)).collect(Collectors.toList());
        byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkeyValueList);
        hbaseTableDataAccessor.delete(rowkeyValue);
    }

    @Override
    public void delete(Object rowkey, String tableName) throws IOException {
        HbaseTableDataAccessor hbaseTableDataAccessor = getHbaseDataAccessor(tableName, hbaseClient);

        byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkey);
        hbaseTableDataAccessor.delete(rowkeyValue);
    }

    @Override
    public void delete(List<Object> rowkeyList, String tableName) throws IOException {
        HbaseTableDataAccessor hbaseTableDataAccessor = getHbaseDataAccessor(tableName, hbaseClient);

        List<byte[]> rowkeyValueList = rowkeyList.stream()
                .map(r -> dataWrapper.convert2Bytes(r)).collect(Collectors.toList());
        byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkeyValueList);
        hbaseTableDataAccessor.delete(rowkeyValue);
    }

    @Override
    public boolean exists(Object rowkey, Class<?> clazz) throws IOException {
        TableDefineInfo tableDefineInfo = Mapper.getTableDefineInfo(clazz);
        checkOrCreateTable(tableDefineInfo);

        HbaseTableDataAccessor hbaseTableDataAccessor =
                getHbaseDataAccessor(tableDefineInfo.getTableName(), hbaseClient);

        byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkey);
        return hbaseTableDataAccessor.exists(rowkeyValue);
    }

    @Override
    public boolean exists(Object rowkey, String tableName) throws IOException {
        HbaseTableDataAccessor hbaseTableDataAccessor = getHbaseDataAccessor(tableName, hbaseClient);

        byte[] rowkeyValue = dataWrapper.convert2Bytes(rowkey);
        return hbaseTableDataAccessor.exists(rowkeyValue);
    }

    private <T> T mapResult(TableDefineInfo tableDefineInfo, Result result) {
        try {
            Object o = tableDefineInfo.getFastClass().newInstance();

            // 绑定数据
            for (ColumnDefineInfo columnDefineInfo : tableDefineInfo.getColumns().values()) {

                // 从result中获取数据并转化成field的对应类型
                Object fieldValue = dataWrapper.unwrap(result, columnDefineInfo.getFamilyName(),
                        columnDefineInfo.getColumnName(), columnDefineInfo.getClazz());

                // 设置默认值
                if (fieldValue == null && !columnDefineInfo.getDefaultValue().equals(Column.NULL_VALUE)) {
                    fieldValue = defaultValueConverter.convertFromString(
                            columnDefineInfo.getDefaultValue(), columnDefineInfo.getClazz());
                }

                // 赋值
                columnDefineInfo.getSetFastMethod().invoke(o, new Object[]{fieldValue});

                // 赋值rowkey
                fieldValue = dataWrapper.unwrapRowkey(result, tableDefineInfo.getRowkeyColumn().getClazz());
                tableDefineInfo.getRowkeyColumn().getSetFastMethod().invoke(o, new Object[]{fieldValue});
            }


            return (T) o;
        } catch (InvocationTargetException ex) {
            throw new MapperException(ex);
        }
    }

    private void checkOrCreateTable(TableDefineInfo tableDefineInfo) {
        boolean autoCreateTable = tableDefineInfo.getTableAnnotation().autoCreateTableDefine();
        checkOrCreateTable(tableDefineInfo.getTableName(), tableDefineInfo.getFamilyName(), autoCreateTable);
    }

    private void checkOrCreateTable(String tableName, String family, boolean autoCreateTable) {
        try {
            Set<String> familyList = hasCheckTableExists.get(tableName);
            if (familyList != null && familyList.contains(family)) {
                return;
            }

            synchronized (this) {
                familyList = hasCheckTableExists.get(tableName);
                if (familyList != null && familyList.contains(family)) {
                    return;
                }

                boolean exists = hbaseDateDefinition.existsTable(tableName);
                if (!exists) {
                    if (autoCreateTable) {
                        hbaseDateDefinition.createTable(tableName, family);
                    } else {
                        throw new MapperException("table:" + tableName + " be created.");
                    }
                } else {
                    byte[] familyValue = dataWrapper.convert2Bytes(family);
                    exists = hbaseDateDefinition.existsFamily(tableName, familyValue);
                    if (!exists) {
                        hbaseDateDefinition.addFamily(tableName, familyValue);
                    }
                }

                familyList = new HashSet<>();
                familyList.add(family);
                hasCheckTableExists.put(tableName, familyList);
            }
        } catch (Exception ex) {
            throw new MapperException(ex);
        }
    }

    public HbaseDataDefinition getHbaseDateDefinition() {
        return hbaseDateDefinition;
    }

    private HbaseTableDataAccessor getHbaseDataAccessor(String tableName, HbaseClient hbaseClient) throws IOException {
        HbaseTableDataAccessor tableDefineInfo = allHbaseTableDataAccessor.get(tableName);
        if (tableDefineInfo != null) {
            return tableDefineInfo;
        }

        HbaseTableDataAccessor newHbaseTableDataAccessor =
                HbaseTableDataAccessorImpl.create(tableName, hbaseClient);
        tableDefineInfo = allHbaseTableDataAccessor.putIfAbsent(tableName, newHbaseTableDataAccessor);
        return tableDefineInfo == null ? newHbaseTableDataAccessor : tableDefineInfo;
    }
}
