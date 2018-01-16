package com.nsn.quick4j.kit;
/**
 * 数组工具类
 * 
 * @author donghao
 * @since 1.0
 */
public class ArrayKit {
	
	/**
	 * 判断数组是否为空
	 * @param array 需要判断的数组
	 * @return true：为空  false：非空
	 */
	public static boolean isEmpty(Object[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * 判断数组是否非空
	 * @param array 需要判断的数组
	 * @return true：非空  false：为空
	 */
	public static boolean isNotEmpty(Object[] array){
		return !isEmpty(array);
	}
}
