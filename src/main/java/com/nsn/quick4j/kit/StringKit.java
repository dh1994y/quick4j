package com.nsn.quick4j.kit;
/**
 * 字符串工具类
 * 
 * @author donghao
 * @since 1.0
 */
public class StringKit {

	/**
	 * 字符串分隔符
	 */
	public static final String SEPARATOR = String.valueOf((char) 29);

	/**
	 * 判断String是否为空
	 * @param str 
	 * @return true:为空  false:非空
	 */
	public static boolean isEmpty(String str){
		//或运算符，截断运算
		return str == null || str.length() == 0;
	}
	
	/**
	 * 判断String是否非空
	 * @param str
	 * @return true:非空  false:为空
	 */
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}

	/**
	 * 若字符串为空，则取默认值
	 */
	public static String defaultIfEmpty(String str, String defaultValue) {
		return isEmpty(str) ? defaultValue : str;
	}
}
