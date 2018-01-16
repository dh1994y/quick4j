package com.nsn.quick4j.orm.annotation;

import com.nsn.quick4j.orm.PKeyPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定主键
 * 保留策略：运行时
 * 标注在：字段上
 * @author donghao
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PKey {

    /**
     * 用于映射数据库主键名
     * @return
     */
    String value();

    /**
     * 主键生成策略
     * @return
     */
    PKeyPolicy pKeyPolicy() default PKeyPolicy.AUTO;
}
