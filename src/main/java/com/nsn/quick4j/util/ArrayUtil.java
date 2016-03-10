package com.nsn.quick4j.util;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

/**
 * 封装数组工具类
 * 
 * @author donghao
 * @since 1.0
 */
public class ArrayUtil {

	public static boolean isEmpty(Object[] array){
		return ArrayUtils.isEmpty(array);
	}
	
	public static boolean isNotEmpty(Object[] array){
		return !isEmpty(array);
	}
	
	public static Object[] concat(Object[] array1,Object[] array2){
		return ArrayUtils.addAll(array1, array2);
	}
}
