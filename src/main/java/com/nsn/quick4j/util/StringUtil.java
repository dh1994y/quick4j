package com.nsn.quick4j.util;
/**
 * 字符串工具类
 * 
 * @author donghao
 * @since 1.0
 */
public class StringUtil {

	/*
	 * 判断String是否为空
	 */
	public static boolean isEmpty(String str){
		//或运算符，截断运算
		return str == null || str.length() == 0;
	}
	/*
	 * 判断String是否非空
	 */
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}

}
