package com.nsn.quick4j;

import com.nsn.quick4j.aop.AopHelper;
import com.nsn.quick4j.core.ClassHelper;
import com.nsn.quick4j.mvc.ControllerHelper;
import com.nsn.quick4j.ioc.BeanHelper;
import com.nsn.quick4j.ioc.IocHelper;
import com.nsn.quick4j.kit.ClassKit;
import com.nsn.quick4j.mvc.converter.ConverterHelper;
import com.nsn.quick4j.orm.EntityHelper;

/**
 * 加載Helper類
 * 
 * @author donghao
 * @since 1.0
 */
public class HelperLoader {

	/**
	 * 初始化：用于加载相应的Helper类
	 */
	public static void init(){
		//声明需要加载的Helper类:执行static code block
		Class<?>[] classArray = {
			ClassHelper.class,
			BeanHelper.class,
			IocHelper.class,
			AopHelper.class,
			ControllerHelper.class,
			ConverterHelper.class,
			EntityHelper.class
		};
		for(Class<?> cls : classArray){
			ClassKit.loadClass(cls.getName());
		}
	}
}
