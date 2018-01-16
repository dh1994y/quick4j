package com.nsn.quick4j.ioc;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.nsn.quick4j.core.ClassHelper;
import com.nsn.quick4j.core.error.InitializationError;
import com.nsn.quick4j.ioc.annotation.Impl;
import com.nsn.quick4j.ioc.annotation.Inject;
import com.nsn.quick4j.kit.CollectionKit;
import com.nsn.quick4j.kit.ArrayKit;
import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.mvc.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 依赖注入助手类
 * 
 * @author donghao
 * @since 1.0
 */
public final class IocHelper {

	/**
	 * 日志记录属性
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(IocHelper.class);
	/**
	 * 静态代码块实现字段注入
	 */
	static{
		init();
	}

	public static void init(){
		try{
			LOGGER.info("[INFO] IocHelper：初始化IOC容器开始");
			//获取beanMap
			Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
			//遍历beanMap
			Set<Entry<Class<?>,Object>> beanEntrySet = beanMap.entrySet();
			for(Entry<Class<?>,Object> entry : beanEntrySet){
				//获取类类对象
				Class<?> cls = entry.getKey();
				//针对非Controller类，因为其为多例，创建时进行注入
				if(cls.isAnnotationPresent(Controller.class)){
					continue;
				}
				//类实例对象
				Object instance = entry.getValue();
				//注入依赖关系
				injectBean(beanMap,cls,instance);
			}
			LOGGER.info("[INFO] IocHelper：初始化IOC容器结束");
		}catch(Exception e){
			//记录错误日志
			LOGGER.error("Initialization IocHelper error!",e);
			//抛出初始化错误
			throw new InitializationError("Initialization IocHelper error!",e);
		}
	}

	/**
	 * controller相关注入，在请求访问期间
	 * @param cls
	 * @param instance
	 */
	public static void injectBean(Map<Class<?>,Object> beanMap,Class<?> cls,Object instance){
		//获取所有字段
		Field[] fields = cls.getDeclaredFields();
		//盘段fields非空
		if(ArrayKit.isNotEmpty(fields)){
			//遍历cls字段，寻找标有@Inject注解，并赋值
			for(Field field : fields){
				//判断是否有@Inject
				if(field.isAnnotationPresent(Inject.class)){
					//获取字段类型
					Class<?> interfaceClass = field.getType();
					//获取字段类型对应实现类
					Class implementClass = findImplementClass(interfaceClass);
					if(implementClass!=null){
						//获取需要注入的实例
						Object injectObj = beanMap.get(implementClass);
						//判断注入的实例非空
						if(injectObj != null){
							//通过反射注入
							ReflectKit.setFieldValue(instance, field, injectObj);
						}else{
							//抛出运行时异常
							throw new RuntimeException("dependency injection failure！className:"+cls.getSimpleName()+",fieldName:"+field);
						}
					}
				}
			}
		}
	}

	/**
	 * 查找指定接口的实现类
	 * @param interfaceClass 指定接口class对象
	 * @return 指定接口的实现类，若未标注impl，则返回本身，标注impl，则判断是否指定实现类，否则返回第一个实现类
     */
	public static Class<?> findImplementClass(Class<?> interfaceClass){
		Class<?> implementClass = interfaceClass;
		//判断接口上是否标注了Impl注解
		if(interfaceClass.isAnnotationPresent(Impl.class)){
			//获取强制指定的实现类
			implementClass = interfaceClass.getAnnotation(Impl.class).value();
		}else{
			//获取该接口所有实现类
			List<Class<?>> implementClassList = ClassHelper.getClassListBySuper(interfaceClass);
			if(CollectionKit.isNotEmpty(implementClassList)){
				//获取第一个实现类
				implementClass = implementClassList.get(0);
			}
		}
		//返回实例对象
		return implementClass;
	}
}
