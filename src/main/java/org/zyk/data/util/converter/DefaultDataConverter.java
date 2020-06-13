package org.zyk.data.util.converter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.zyk.data.util.exception.DataConvertException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 默认的数据转换器（基础实现）
 */
public class DefaultDataConverter implements DataConverter {
    public static DataConverter create() {
        return new DefaultDataConverter();
    }

    public Object data2JavaObject(byte[] bytes, Class<?> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        String className = clazz.getCanonicalName();

        try {
            switch (className) {
                case "java.lang.String":
                    return Bytes.toString(bytes);

                case "java.lang.Byte[]":
                    return ArrayUtils.toObject(bytes);

                case "java.lang.Integer":
                case "int":
                    return Bytes.toInt(bytes);

                case "java.lang.Short":
                case "short":
                    return Bytes.toShort(bytes);

                case "java.lang.Float":
                case "float":
                    return Bytes.toFloat(bytes);

                case "java.lang.Boolean":
                case "boolean":
                    return Bytes.toBoolean(bytes);

                case "java.lang.Double":
                case "double":
                    return Bytes.toDouble(bytes);

                case "java.lang.Long":
                case "long":
                    return Bytes.toLong(bytes);

                case "java.lang.Character":
                case "char":
                    return Bytes.toString(bytes).charAt(0);

                case "java.math.BigDecimal":
                    return Bytes.toBigDecimal(bytes);

                case "java.util.Date":
                    return new Date(Bytes.toLong(bytes));

                case "java.util.Calendar":
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Bytes.toLong(bytes));
                    return calendar;

                case "java.time.LocalDate":
                    return Instant.ofEpochMilli(Bytes.toLong(bytes))
                            .atZone(ZoneId.systemDefault()).toLocalDate();

                case "java.time.LocalDateTime":
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(Bytes.toLong(bytes)),
                            TimeZone.getDefault().toZoneId());
            }
        } catch (Exception ex) {
            throw new DataConvertException(ex);
        }

        throw new DataConvertException("unsupport convert type:" + clazz);
    }

    public byte[] javaObject2Data(Object data) {
        if (data == null) {
            return null;
        }

        String type = data.getClass().getCanonicalName();

        try {
            switch (type) {
                case "java.lang.String":
                    return Bytes.toBytes((String) data);

                case "java.lang.Byte[]":
                    return ArrayUtils.toPrimitive((Byte[]) data);

                case "java.lang.Integer":
                    return Bytes.toBytes((Integer) data);

                case "java.lang.Boolean":
                    return Bytes.toBytes((Boolean) data);

                case "java.lang.Short":
                    return Bytes.toBytes((Short) data);

                case "java.lang.Float":
                    return Bytes.toBytes((Float) data);

                case "java.lang.Double":
                    return Bytes.toBytes((Double) data);

                case "java.lang.Long":
                    return Bytes.toBytes((Long) data);

                case "java.lang.Character":
                    return Bytes.toBytes(String.valueOf(data));

                case "java.math.BigDecimal":
                    return Bytes.toBytes((BigDecimal) data);

                case "java.util.Date":
                    return Bytes.toBytes(((Date) data).getTime());

                case "java.util.Calendar":
                    return Bytes.toBytes(((Calendar) data).getTimeInMillis());

                case "java.time.LocalDate":
                    return Bytes.toBytes(((LocalDate) data).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

                case "java.time.LocalDateTime":
                    return Bytes.toBytes(((LocalDateTime) data).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }

        } catch (Exception ex) {
            throw new DataConvertException(ex);
        }

        throw new DataConvertException("unsupport convert type:" + type);
    }
}
