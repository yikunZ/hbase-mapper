package org.zyk.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示hbase的列（定义在字段中的）
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String NULL_VALUE = "&%*NULL(~?";

    /**
     * 列名（默认为空，此时列名为字段名称）
     */
    String name() default "";

    /**
     * 默认值（不设置时为无默认值，由于无法直接定义为为null，这边采用了一个比较奇葩的字符串表示为null）
     * 作用：当从hbase中获取不到改列数据时会自动设置成这个值
     * 默认值是String类型，会根据字段的类型转化成相应类型，例如：
     *  @Column(defaultValue = "1")
     *  private Integer column1;
     */
    String defaultValue() default NULL_VALUE;
}
