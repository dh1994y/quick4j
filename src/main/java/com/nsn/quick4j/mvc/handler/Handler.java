package com.nsn.quick4j.mvc.handler;

import java.lang.reflect.Method;

/**
 * 封装Action信息
 * @author donghao
 * @since 1.0
 */
public class Handler {
	
	/**
	 * action所在的Controller类
	 */
	private Class<?> controllerClass;
	
	/**
	 * action对应的方法
	 */
	private Method actionMethod;
	
	/**
	 * 构造方法
	 * @param controllerClass
	 * @param actionMethod
	 */
	public Handler(Class<?> controllerClass, Method actionMethod) {
		this.controllerClass = controllerClass;
		this.actionMethod = actionMethod;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}
}
