package org.zyk.data.hbase.mapper.converter;

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
    public byte[] convert2Bytes(Object obj) {
        return dataConverter.metaDataConvert2Bytes(obj);
    }

    public Object unwrapRowkey(Result result, Class<?> clazz) {
        byte[] value = result.getRow();
        if (value == null || Arrays.equals(value, NULL_VALUE)) {
            return null;
        }

        return dataConverter.bytes2JavaObject(value, clazz);
    }

    public Object unwrap(Result result, Object familyName, Object columnName, Class<?> clazz) {
        byte[] familyNameBytes = dataConverter.metaDataConvert2Bytes(familyName);
        byte[] columnNameBytes = dataConverter.metaDataConvert2Bytes(columnName);
        byte[] value = result.getValue(familyNameBytes, columnNameBytes);
        if (value == null || Arrays.equals(value, NULL_VALUE)) {
            return null;
        }

        return dataConverter.bytes2JavaObject(value, clazz);
    }

    public Put wrap(Put put, Object familyName, Object columnName, Object value, boolean saveNullValue) {
        byte[] familyNameBytes = dataConverter.metaDataConvert2Bytes(familyName);
        byte[] columnNameBytes = dataConverter.metaDataConvert2Bytes(columnName);

        if (saveNullValue && value == null) {
            put.addColumn(familyNameBytes, columnNameBytes, NULL_VALUE);
            return put;
        }

        byte[] bytes = dataConverter.javaObject2Bytes(value);
        put.addColumn(familyNameBytes, columnNameBytes, bytes);
        return put;
    }
}
