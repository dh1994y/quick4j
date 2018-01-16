package com.nsn.quick4j.mvc.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装视图对象
 * 
 * @author donghao
 * @since 1.0
 */
public class View {
	
	/**
	 * 视图路径
	 */
	private String path;
	/**
	 * 模型数据
	 */
	private Map<String, Object> model;
	
	/**
	 * 构造方法
	 * @param path
	 */
	public View(String path){
		this.path = path;
		model = new HashMap<String, Object>();
	}
	
	/**
	 * 添加模型数据
	 * @param key 键
	 * @param value 值
	 * @return 返回当前视图对象，用于链式编程
	 */
	public View addModel(String key, Object value){
		model.put(key, value);
		return this;
	}

	/**
	 * 获取视图路径
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 获取当前视图模型数据
	 * @return
	 */
	public Map<String, Object> getModel() {
		return model;
	}

	/**
	 * 判断当前View是否是重定向，若/开始则是重定向
	 * @return
     */
	public boolean isRedirect() {
		return path.startsWith("/");
	}
}
