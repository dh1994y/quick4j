package com.nsn.quick4j.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识该字段为额外字段不做映射处理
 * 保留策略：运行时
 * 标注在： 字段上
 * @author donghao
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Extra {
}
