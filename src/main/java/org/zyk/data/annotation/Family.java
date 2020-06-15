package org.zyk.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示列簇
 * 可定义在表对应的类上，或字段上
 * 当定义在类上时，那么该类中的所有字段获取时都会使用类上的定义
 * 当定义在字段上时，该字段会使用该定义获取列簇
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Family {
    String defaultValue = "cf";

    /**
     * 列簇名称
     */
    String value() default "cf";
}
