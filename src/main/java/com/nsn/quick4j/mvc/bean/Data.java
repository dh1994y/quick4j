package com.nsn.quick4j.mvc.bean;
/**
 * 封装数据对象（Json数据）
 * 
 * @author donghao
 * @since 1.0
 */
public class Data {

	/**
	 * 模型数据
	 */
	private Object model;
	
	/**
	 * 构造方法
	 * @param model
	 */
	public Data(Object model) {
		this.model = model;
	}

	/**
	 * 获取模型数据
	 * @return
	 */
	public Object getModel() {
		return model;
	}
}
