package org.zyk.data.util.converter;

import org.apache.hadoop.hbase.util.Bytes;
import org.zyk.data.util.exception.DataConvertException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 所有数据都先经过转化成String中间对象后再转化成对应类型的转化器
 */
public class StringStoreDataConverter implements DataConverter {
    private static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static ThreadLocal<SimpleDateFormat> dateFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat(DEFAULT_DATE_FORMAT));

    private static ThreadLocal<SimpleDateFormat> dateTimeFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat(DEFAULT_DATETIME_FORMAT));

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);

    public static DataConverter create() {
        return new StringStoreDataConverter();
    }

    public Object data2JavaObject(byte[] bytes, Class<?> clazz) {
        String className = clazz.getCanonicalName();

        try {
            if (bytes == null) {
                return null;
            }

            String stringValue = Bytes.toString(bytes);
            switch (className) {
                case "java.lang.String":
                    return stringValue;

                case "java.lang.Integer":
                case "int":
                    return Integer.valueOf(stringValue);

                case "java.lang.Short":
                case "short":
                    return Short.valueOf(stringValue);

                case "java.lang.Float":
                case "float":
                    return Float.valueOf(stringValue);

                case "java.lang.Double":
                case "double":
                    return Double.valueOf(stringValue);

                case "java.lang.Long":
                case "long":
                    return Long.valueOf(stringValue);

                case "java.lang.Character":
                case "char":
                    return stringValue.charAt(0);

                case "java.math.BigDecimal":
                    return new BigDecimal(stringValue);

                case "java.util.Date":
                    return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(stringValue);

                case "java.util.Calendar":
                    Date date = dateTimeFormatHolder.get().parse(stringValue);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    return cal;

                case "java.time.LocalDate":
                    return LocalDate.parse(stringValue, dateFormatter);

                case "java.time.LocalDateTime":
                    return LocalDate.parse(stringValue, dateTimeFormatter);
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

        String stringValue = null;

        switch (type) {
            case "java.lang.String":
            case "java.lang.Integer":
            case "java.lang.Short":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.lang.Long":
            case "java.lang.Character":
            case "java.math.BigDecimal":
                stringValue = data.toString();
                break;

            case "java.util.Date":
                stringValue = dateFormatHolder.get().format(data);
                break;
            case "java.util.Calendar":
                stringValue = dateTimeFormatHolder.get().format(data);
                break;

            case "java.time.LocalDate":
                stringValue = dateFormatter.format((LocalDate) data);
                break;

            case "java.time.LocalDateTime":
                stringValue = dateTimeFormatter.format((LocalDate) data);
        }

        if (stringValue == null) {
            throw new DataConvertException("unsupport convert type:" + type);
        }

        return Bytes.toBytes(stringValue);
    }
}
