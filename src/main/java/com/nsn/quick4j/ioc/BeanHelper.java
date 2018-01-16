package com.nsn.quick4j.ioc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsn.quick4j.aop.AopHelper;
import com.nsn.quick4j.aop.annotation.Aspect;
import com.nsn.quick4j.aop.proxy.IAspect;
import com.nsn.quick4j.aop.proxy.ProxyManager;
import com.nsn.quick4j.aop.tx.annotation.Service;
import com.nsn.quick4j.core.ClassHelper;
import com.nsn.quick4j.core.error.InitializationError;
import com.nsn.quick4j.ioc.annotation.Bean;
import com.nsn.quick4j.mvc.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nsn.quick4j.kit.ReflectKit;

/**
 * Bean操作助手类
 * 
 * @author donghao
 * @since 1.0
 */
public final class BeanHelper {
	
	/**
	 * 日志记录对象
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanHelper.class);
	/**
	 * 定义Bean映射：用于存放Bean Class对象与 Bean类的实例的映射关系
	 * 包括 Bean Service Controller Aspect
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

	/**
	 * 静态代码块实例化基础包下所有类
	 */
	static{
		init();
	}

	public static void init(){
		try{
			LOGGER.info("[INFO] BeanHelper：实例化Bean开始");
			//获取beanClass
			List<Class<?>> beanClassList = ClassHelper.getClassList();
			//遍历并实例化并加入映射容器
			for(Class<?> cls : beanClassList){

				if(cls.isAnnotationPresent(Controller.class)){
					//放入beanMap Controller针对请求时才去创建
					BEAN_MAP.put(cls, null);
				}else if(cls.isAnnotationPresent(Bean.class)||
						cls.isAnnotationPresent(Service.class)){
					//判断是否是代理对象
					Object obj = null;
					List<IAspect> aspectList = AopHelper.getAspectProxyList(cls);
					if(aspectList != null){
						//创建代理对象
						obj = ProxyManager.createProxy(cls, aspectList);
					}else{
						//创建Bean实例对象
						obj = ReflectKit.newInstance(cls);
					}
					//放入beanMap  key(xxx.class):value(instance)
					BEAN_MAP.put(cls, obj);
				}else if(cls.isAnnotationPresent(Aspect.class)){
					BEAN_MAP.put(cls, AopHelper.getAspectInstance(cls));
				}
			}
			LOGGER.info("[INFO] BeanHelper：实例化Bean结束");
		}catch (Exception e) {
			//记录错误日志
			LOGGER.error("Initialization BeanHelper error！");
			//抛出初始化错误
			throw new InitializationError("Initialization BeanHelper error！",e);
		}
	}

	/**
	 * 获取Bean映射容器
	 * @return bean映射容器
	 */
	public static Map<Class<?>, Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	/**
	 * 获取指定类类型的bean实例对象
	 * @param cls 指定类类型
	 * @return 指定类类型的实例对象，若不存在指定类型，则抛出异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls){
		if(!BEAN_MAP.containsKey(cls)){
			//错误日志记录
			LOGGER.error("can not get bean by class:" + cls);
			//抛出运行时异常
			throw new RuntimeException("can not get bean by class:" + cls);
		}
		return (T)BEAN_MAP.get(cls);
	}

	/**
	 * 设置Bean实例
	 * 将给定Bean实例放入BEAN_MAP
	 * @param cls 类 Class 对象
	 * @param obj 实例对象
     */
	public static void setBean(Class<?> cls,Object obj){
		BEAN_MAP.put(cls,obj);
	}
}
