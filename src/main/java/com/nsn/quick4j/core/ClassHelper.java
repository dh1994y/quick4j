package com.nsn.quick4j.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.nsn.quick4j.core.impl.DefaultClassScanner;
import com.nsn.quick4j.mvc.annotation.Controller;
import com.nsn.quick4j.aop.tx.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具助手类
 * 
 * @author donghao
 * @since 1.0
 */
public final class ClassHelper {

	/**
	 * 类扫描器
	 */
	private static final ClassScanner CLASS_SCANNER = new DefaultClassScanner();
	/**
	 * 基础包名
	 */
	private static final String BASE_PACKAGE = ConfigHelper.getAppBasePackage();
	
	/**
	 * 获取应包名下的所有类
	 * 实现热部署
	 * @return class容器
	 */
	public static List<Class<?>> getClassList(){
		return CLASS_SCANNER.getClassList(BASE_PACKAGE);
	}

	/**
	 * 获取应用包名下的指定注解的类
	 * @param annotationClass 指定注解
	 * @return 满足条件的类集合
     */
	public static List<Class<?>> getClassListByAnnotation(Class<? extends Annotation> annotationClass){
		return CLASS_SCANNER.getClassListByAnnotation(BASE_PACKAGE,annotationClass);
	}

	/**
	 * 获取应用包名下的指定父类或接口的类
	 * @param superClass 指定父类或接口
	 * @return 满足条件的类集合
     */
	public static List<Class<?>> getClassListBySuper(Class<?> superClass){
		return CLASS_SCANNER.getClassListBySuper(BASE_PACKAGE,superClass);
	}

	/**
	 * 获取应用包名下的所有Service类
	 * @return service类 类对象 容器
	 */
	public static List<Class<?>> getServiceClassList(){
		return CLASS_SCANNER.getClassListByAnnotation(BASE_PACKAGE,Service.class);
	}
	
	/**
	 * 获取应用包名下所有Controller类
	 * @return controller类 类对象 容器
	 */
	public static List<Class<?>> getControllerClassList(){
		return CLASS_SCANNER.getClassListByAnnotation(BASE_PACKAGE,Controller.class);
	}
	
	/**
	 * 获取应用报名下的所有Bean类
	 * @return service、controller类 类对象 容器
	 */
	public static List<Class<?>> getBeanClassList(){
		//声明容器
		List<Class<?>> beanClassList = new ArrayList<Class<?>>();
		//添加类对象到容器
		beanClassList.addAll(getServiceClassList());
		beanClassList.addAll(getControllerClassList());
		//返回
		return beanClassList;
	}
}
