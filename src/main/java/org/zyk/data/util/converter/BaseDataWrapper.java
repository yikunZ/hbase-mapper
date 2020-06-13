package org.zyk.data.util.converter;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;

public class BaseDataWrapper implements DataWrapper {
    private static byte[] NULL_VALUE = Bytes.toBytes("&%*NULL(~?");
    private DataConverter dataConverter;

    public static DataWrapper create(DataConverter dataConverter) {
        return new BaseDataWrapper(dataConverter);
    }

    BaseDataWrapper(DataConverter dataConverter) {
        this.dataConverter = dataConverter;
    }

    @Override
    public byte[] convert2MetaData(Object obj) {
        return dataConverter.convert2MetaData(obj);
    }

    public Object unwrapRowkey(Result result, Class<?> clazz) {
        byte[] value = result.getRow();
        if (value == null || Arrays.equals(value, NULL_VALUE)) {
            return null;
        }

        return dataConverter.data2JavaObject(value, clazz);
    }

    public Object unwrap(Result result, Object familyName, Object columnName, Class<?> clazz) {
        byte[] familyNameBytes = dataConverter.convert2MetaData(familyName);
        byte[] columnNameBytes = dataConverter.convert2MetaData(columnName);
        byte[] value = result.getValue(familyNameBytes, columnNameBytes);
        if (value == null || Arrays.equals(value, NULL_VALUE)) {
            return null;
        }

        return dataConverter.data2JavaObject(value, clazz);
    }

    public Put wrap(Put put, Object familyName, Object columnName, Object value, boolean saveNullValue) {
        byte[] familyNameBytes = dataConverter.convert2MetaData(familyName);
        byte[] columnNameBytes = dataConverter.convert2MetaData(columnName);

        if (saveNullValue && value == null) {
            put.addColumn(familyNameBytes, columnNameBytes, NULL_VALUE);
            return put;
        }

        byte[] bytes = dataConverter.javaObject2Data(value);
        put.addColumn(familyNameBytes, columnNameBytes, bytes);
        return put;
    }
}
