package com.nsn.quick4j.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsn.quick4j.core.ClassHelper;
import com.nsn.quick4j.mvc.bean.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nsn.quick4j.mvc.annotation.Action;
import com.nsn.quick4j.mvc.annotation.Controller;
import com.nsn.quick4j.mvc.handler.Handler;
import com.nsn.quick4j.kit.ArrayKit;

/**
 * 控制器助手类 用于初始化路径映射以及获取指定映射
 * 
 * @author donghao
 * @since 1.0
 */
public final class ControllerHelper {

	/**
	 * 日志记录属性
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
	/**
	 * 用于存储请求与处理器之间的映射关系
	 */
	private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

	/**
	 * 静态代码块初始化映射关系
	 */
	static {
		init();
	}

	public static void init(){
		LOGGER.info("[INFO] ControllerHelper：初始化映射容器：ACTION_MAP 开始");
		// 获取所有Controller类
		List<Class<?>> controllerClassList = ClassHelper.getControllerClassList();
		// 遍历Controller类用于获取action方法
		for (Class<?> cls : controllerClassList) {
			// 获取cls方法数组
			Method[] methods = cls.getDeclaredMethods();
			// 非空校验
			if (ArrayKit.isNotEmpty(methods)) {
				// 遍历方法，判断是否有action注解
				for (Method method : methods) {
					if (method.isAnnotationPresent(Action.class)) {
						// 从action注解中获取url映射规则
						Action action = method.getAnnotation(Action.class);
						// 获取映射值
						String mapping = action.value();
						// 正则验证url映射规则
						if (mapping.matches("\\w+:(/\\w+)+")) {
							String[] array = mapping.split(":");
							// 合法性校验
							if (ArrayKit.isNotEmpty(array) && array.length == 2) {
								// 创建Request对象
								String requestMethod = array[0].toLowerCase();
								String requestPath = array[1];
								Request request = new Request(requestMethod, requestPath);
								//异常检查，若request映射重复，则抛出异常
								if(ACTION_MAP.containsKey(request)){
									//记录错误日志
									LOGGER.error("mapping path repeat:"+request);
									//抛出运行时异常
									throw new RuntimeException("mapping path repeat:"+request);
								}
								// 创建Handler对象
								Handler handler = new Handler(cls, method);
								// 加入ACTION_MAP
								ACTION_MAP.put(request, handler);
							}
						}
					}
				}
			}
		}
		LOGGER.info("[INFO] ControllerHelper：初始化映射容器：ACTION_MAP 结束");
	}

	/**
	 * 根据请求路径和请求方法获取指定的处理器
	 * 
	 * @param requestMethod
	 *            请求方法
	 * @param requestPath
	 *            请求路径
	 * @return 对应处理器
	 */
	public static Handler getHandler(String requestMethod, String requestPath) {
		// 封装request对象
		Request request = new Request(requestMethod, requestPath);
		// 从ACTION_MAP中查找
		return ACTION_MAP.get(request);
	}
	
	/**
	 * 获取映射容器
	 * @return ACTION_MAP
	 */
	public static Map getActionMap() {
		return ACTION_MAP;
	}
}
