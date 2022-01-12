package org.zyk.data.hbase.mapper.converter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.zyk.data.hbase.mapper.exception.DataConvertException;

/**
 * 数据转化
 * hbase中存取的对象都是使用byte[]表示，需要这么一个数据转化器来对byte[]和java类型的互相转化操作
 */
public interface DataConverter {
    /**
     * 用于将用户定义的元数据转化成hbase客户端能使用byte数组
     * 这边的元数据包括：表名、列簇名等
     *
     * @param data 需要转化的元数据
     * @return byte数组表示的元数据
     */
    default byte[] metaDataConvert2Bytes(Object data) {
        String type = data.getClass().getCanonicalName();
        switch (type) {
            case "byte[]":
                return (byte[]) data;

            case "java.lang.String":
                return Bytes.toBytes((String) data);

            case "java.lang.Byte[]":
                return ArrayUtils.toPrimitive((Byte[]) data);

            case "java.lang.Integer":
            case "int":
                return Bytes.toBytes((int) data);

            case "java.lang.Short":
            case "short":
                return Bytes.toBytes((short) data);

            case "java.lang.Float":
            case "float":
                return Bytes.toBytes((float) data);

            case "java.lang.Double":
            case "double":
                return Bytes.toBytes((double) data);

            case "java.lang.Long":
            case "long":
                return Bytes.toBytes((long) data);

            case "java.lang.Character":
            case "char":
                return Bytes.toBytes(String.valueOf(data));
        }

        throw new DataConvertException("unsupported convert metadata type:" + type);
    }

    /**
     * 将hbase中的byte数组表示的列数据转化成对应的java类型
     *
     * @param bytes 要转化的byte数组
     * @param type  对应的java类型
     * @return 转化后的java类型对象
     */
    Object bytes2JavaObject(byte[] bytes, Class<?> type);

    /**
     * 将java类型对象转化成hbase能够存储的byte数组
     *
     * @param data 要转化的java类型对象
     * @return 转化后的byte数组对象
     */
    byte[] javaObject2Bytes(Object data);
}
