package com.nsn.quick4j.util;

 import org.apache.commons.lang.ArrayUtils;

/**
 * 封装数组工具类
 * 
 * @author donghao
 * @since 1.0
 */
public class ArrayUtil {

	/*
	 * 判断数组是否为空
	 */
	public static boolean isEmpty(Object[] array){
		return ArrayUtils.isEmpty(array);
	}
	/*
	 * 判断数组是否非空
	 */
	public static boolean isNotEmpty(Object[] array){
		return !isEmpty(array);
	}
	/*
	 * 连接两个数组
	 */
	public static Object[] concat(Object[] array1,Object[] array2){
		return ArrayUtils.addAll(array1, array2);
	}
	/*
	 * 判断数组是否包含某个元素
	 */
	public static boolean contains(Object[] array,Object valueToFind){
		return ArrayUtils.contains(array, valueToFind);
	}
}
