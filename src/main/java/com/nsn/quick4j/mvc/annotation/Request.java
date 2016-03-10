package com.nsn.quick4j.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Request注解，用于定义请求方式
 * 
 * @author donghao
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Request {

	/**
	 * 定义GET请求方式
	 * 
	 * @author donghao
	 * @since 1.0
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface GET{
		String value();
	}
	/**
	 * 定义POST请求方式
	 * 
	 * @author donghao
	 * @since 1.0
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface POST{
		String value();
	}
	/**
	 * 定义PUT请求方式
	 * 
	 * @author donghao
	 * @since 1.0
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface PUT{
		String value();
	}
	/**
	 * 定义DELETE请求方式
	 * 
	 * @author donghao
	 * @since 1.0
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DELETE{
		String value();
	}
}
