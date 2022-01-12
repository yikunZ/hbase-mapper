package org.zyk.data.hbase.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name();

    /**
     * 是否处理null值
     * hbase没有对null值的特殊定义，它用存不存在一个列表示是否为null值
     * 那么一旦put过这个列的数据后，后面即使put时设置为null，hbase再重新获取时也无法表示为null
     * 这种情况对于String来说是致命的，因为这样会导致String无法区分存储空字符串和null
     * 所以程序中采用了存储一个特殊字符串来表示null值，当然这个字符串得足够特殊才能避免跟正常存储的字符串冲突
     */
    boolean nullValueHandle() default false;

    /**
     * 是否自动创建表
     * 程序会先判断是否存在表定义，如果不存在将根据表定义自动创建对应的表和类簇
     * 当用户没有手动初始化表或列簇时该值设置为false时，运行数据存取操作将会抛出hhbase无法找到表或列簇定义的异常
     */
    boolean autoCreateTableDefine() default true;
}
