package com.nsn.quick4j.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义切面的执行顺序
 * 保留策略：运行时
 * 标注在类上
 * @author donghao
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AspectOrder {
    /**
     * 切面执行顺序
     * @return
     */
    int value();
}
