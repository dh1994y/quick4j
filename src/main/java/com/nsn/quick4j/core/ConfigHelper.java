package com.nsn.quick4j.core;

import java.util.Properties;

import com.nsn.quick4j.FrameworkConstant;
import com.nsn.quick4j.kit.PropsKit;

/**
 * 获取常量帮助类
 * 
 * @author donghao
 * @since 1.0
 */
public final class ConfigHelper {
	//常量配置文件对象
	private static final Properties CONFIG_PROPS = PropsKit.loadProps(FrameworkConstant.CONFIG_FILE);

	/*
	 * 获取数据库用户名
	 */
	public static String getJdbcUsername(){
		return CONFIG_PROPS.getProperty(FrameworkConstant.JDBC_USERNAME);
	}
	
	/*
	 * 获取数据库密码
	 */
	public static String getJdbcPassword(){
		return CONFIG_PROPS.getProperty(FrameworkConstant.JDBC_PASSWORD);
	}
	
	/*
	 * 获取数据库连接url
	 */
	public static String getJdbcUrl(){
		return CONFIG_PROPS.getProperty(FrameworkConstant.JDBC_URL);
	}
	
	/*
	 * 获取数据库连接驱动类名
	 */
	public static String getJdbcDriver(){
		return CONFIG_PROPS.getProperty(FrameworkConstant.JDBC_DRIVER);
	}
	
	/*
	 * 获取应用基础包名
	 */
	public static String getAppBasePackage(){
		return CONFIG_PROPS.getProperty(FrameworkConstant.APP_BASE_PACKAGE);
	}
	
	/*
	 * 获取应用基础JSP路径
	 */
	public static String getAppJspPath(){
		return CONFIG_PROPS.getProperty(FrameworkConstant.APP_JSP_PATH);
	}
	
	/*
	 * 获取应用基础静态资源路径
	 */
	public static String getAppAssetPath(){
		return CONFIG_PROPS.getProperty(FrameworkConstant.APP_ASSET_PATH);
	}

	/*
	 * 获取是否打印sql语句
	 */
	public static Boolean isShowSql(){
		return "true".equals(CONFIG_PROPS.getProperty(FrameworkConstant.SHOW_SQL));
	}

	/*
	 * 获取app 首页
	 */
	public static String getAppHomePage(){
		return CONFIG_PROPS.getProperty(FrameworkConstant.APP_HOME_PAGE);
	}
}
