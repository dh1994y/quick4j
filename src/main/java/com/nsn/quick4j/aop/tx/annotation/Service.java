package com.nsn.quick4j.aop.tx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注服务类注解的类将被代理，会被事务切面拦截
 * 服务类注解
 * 标注在类上
 * 保留策略：运行时
 * @author donghao
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

}
