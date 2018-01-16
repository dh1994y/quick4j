package com.nsn.quick4j.mvc.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 类型转换类型：用于描述转换的目标类型
 * 保留策略：运行时
 * 标注在类上
 * @author donghao
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConverterType {

    /**
     * 目标转型类型
     * @return
     */
    Class<?>[] value();
}
