package org.zyk.data.converter;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

/**
 * 对数据进行包装和解包
 */
public interface DataWrapper {
    byte[] convert2MetaData(Object obj);

    /**
     * 解包获取rowkey
     *
     * @param result     hbase查询结果
     * @param clazz       要获取得到的最终类型
     * @return 解包后的数据实例
     */
    Object unwrapRowkey(Result result, Class<?> clazz);

    /**
     * 解包
     *
     * @param result     hbase查询结果
     * @param familyName 要获取的family名称
     * @param columnName 要获取的列名称
     * @param clazz       要获取得到的最终类型
     * @return 解包后的数据实例
     */
    Object unwrap(Result result, Object familyName, Object columnName, Class<?> clazz);

    /**
     * 打包
     *
     * @param put           打包成的put对象
     * @param familyName    打包的family名称
     * @param columnName    打包的列名称
     * @param value         打包的值
     * @param saveNullValue 是否存储空值
     * @return 打包后最终的put对象
     */
    Put wrap(Put put, Object familyName, Object columnName, Object value, boolean saveNullValue);
}
