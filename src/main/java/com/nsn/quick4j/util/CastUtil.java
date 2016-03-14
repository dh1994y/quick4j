package com.nsn.quick4j.util;

/**
 * 类型转换工具类
 * 提供long、int、double、boolean、String类型格式转换
 * 提供转换默认值、以及默认值重载方法
 * 
 * @author donghao
 * @since 1.0
 */
public class CastUtil {

	/*
	 * 转换成String，提供默认值
	 */
	public static String castString(Object obj,String defaultValue){
		//为空返回默认值，否则返回toString方法转换值
		return obj != null ? String.valueOf(obj) : defaultValue;
	}
	/*
	 * 默认值""的重载方法
	 */
	public static String castString(Object obj){
		return castString(obj,"");
	}
	
	/*
	 * 转化为long类型，提供默认值
	 */
	public static long castLong(Object obj,long defaultValue){
		/*
		 * 判断obj非空
		 * 考虑long格式字符串
		 * 先转换为字符串，再转换为long，考虑类型转换异常
		 */
		long longValue = defaultValue;
		if(obj != null){
			String strValue = castString(obj);
			try {
				longValue = Long.parseLong(strValue);
			} catch (NumberFormatException e) {
				//屏蔽类型转换异常，使其为默认值
			}
		}
		return longValue;
	}
	/*
	 * 转化为long，默认值0
	 */
	public static long castLong(Object obj){
		return castLong(obj,0);
	}
	
	/*
	 * 转化为int，提供默认值
	 */
	public static int castInt(Object obj,int defaultValue){
		/*
		 * 判断obj是否为空
		 * 考虑int格式字符串
		 * 先转为字符串，注意考虑类型转化失败异常
		 */
		int intValue = defaultValue;
		if(obj!=null){
			String strValue = castString(obj);
			try {
				intValue = Integer.parseInt(strValue);
			} catch (NumberFormatException e) {
				//屏蔽类型转换异常，使其为默认值
			}
		}
		return intValue;
	}
	/*
	 * 转化为int，默认值 0
	 */
	public static int castInt(Object obj){
		return castInt(obj,0);
	}
	
	/*
	 * 转化为double，提供默认值
	 */
	public static double castDouble(Object obj,double defaultValue){
		/*
		 * 判断obj是否为空
		 * 考虑double格式字符串
		 * 先转为字符串，注意考虑类型转化失败异常
		 */
		double doubleValue = defaultValue;
		if(obj!=null){
			String strValue = castString(obj);
			try {
				doubleValue = Double.parseDouble(strValue);
			} catch (NumberFormatException e) {
				//屏蔽类型转换异常，使其为默认值
			}
		}
		return doubleValue;
	}
	/*
	 * 转化为double，默认值0
	 */
	public static double castDouble(Object obj){
		return castDouble(obj,0);
	}
	
	/*
	 * 转化为boolean，提供默认值
	 */
	public static boolean castBoolean(Object obj,boolean defaultValue){
		/*
		 * 判断obj是否为空
		 * 考虑boolean格式字符串
		 * 先转为String,再转成boolean
		 * 无需考虑类型转化异常，jdk源码非true即为false
		 */
		boolean boolValue = defaultValue;
		if(obj!=null){
			boolValue = Boolean.parseBoolean(castString(obj));
		}
		return boolValue;
	}
	/*
	 * 转化为boolean，默认值false
	 */
	public static boolean castBoolean(Object obj){
		return castBoolean(obj,false);
	}
	
	/*
	 * 转换为String[]数组
	 */
	public static String[] castStringArray(Object[] objArray){
		/*
		 * 判断objArray是否为空，为空初始化0长度
		 * 非空开始转换
		 */
		if(objArray == null){
			objArray = new Object[0];
		}
		String[] strArray = new String[objArray.length];
		//判断非空以及长度非0
		if(ArrayUtil.isNotEmpty(objArray)){
			for(int i=0;i<objArray.length;i++){
				strArray[i] = castString(objArray[i]);
			}
		}
		return strArray;
	}
	
	/*
	 * 转换为long[]数组
	 */
	public static long[] castLongArray(Object[] objArray){
		/*
		 * 判断objArray是否为空，为空初始化0长度
		 * 非空开始转换
		 */
		if(objArray == null){
			objArray = new Object[0];
		}
		long[] longArray = new long[objArray.length];
		if(ArrayUtil.isNotEmpty(objArray)){
			for(int i=0;i<objArray.length;i++){
				longArray[i] = castLong(objArray[i]);			}
		}
		return longArray;
	}
	/*
	 * 转换为int[]数组
	 */
	public static int[] castIntArray(Object[] objArray){
		/*
		 * 判断objArray是否为空，为空初始化0长度
		 * 非空开始转换
		 */
		if(objArray == null){
			objArray = new Object[0];
		}
		int[] intArray = new int[objArray.length];
		if(ArrayUtil.isNotEmpty(objArray)){
			for(int i=0;i<objArray.length;i++){
				intArray[i] = castInt(objArray[i]);			}
		}
		return intArray;
	}
	/*
	 * 转换为double[]数组
	 */
	public static double[] castDoubleArray(Object[] objArray){
		/*
		 * 判断objArray是否为空，为空初始化0长度
		 * 非空开始转换
		 */
		if(objArray == null){
			objArray = new Object[0];
		}
		double[] doubleArray = new double[objArray.length];
		if(ArrayUtil.isNotEmpty(objArray)){
			for(int i=0;i<objArray.length;i++){
				doubleArray[i] = castDouble(objArray[i]);			}
		}
		return doubleArray;
	}
	/*
	 * 转换为boolean[]数组
	 */
	public static boolean[] castBooleanArray(Object[] objArray){
		/*
		 * 判断objArray是否为空，为空初始化0长度
		 * 非空开始转换
		 */
		if(objArray == null){
			objArray = new Object[0];
		}
		boolean[] boolArray = new boolean[objArray.length];
		if(ArrayUtil.isNotEmpty(objArray)){
			for(int i=0;i<objArray.length;i++){
				boolArray[i] = castBoolean(objArray[i]);			}
		}
		return boolArray;
	}
}
