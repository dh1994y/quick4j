package com.nsn.quick4j.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Action方法注解
 * 标注在方法上
 * 保留策略：运行时
 * @author donghao
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

	/**
	 * @return 请求类型与路径
	 */
	String value();
}
