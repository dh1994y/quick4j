package com.nsn.quick4j.mvc.bean;

import com.nsn.quick4j.core.bean.BaseBean;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 封装请求对象
 * 	包括请求方法、请求路径
 * 
 * @author donghao
 * @since 1.0
 */
public class Request extends BaseBean{

	/**
	 * 请求方法
	 */
	private String requestMethod;
	
	/**
	 * 请求路径
	 */
	private String requestPath;

	/**
	 * 构造方法
	 * @param requestMethod
	 * @param requestPath
	 */
	public Request(String requestMethod, String requestPath) {
		this.requestMethod = requestMethod;
		this.requestPath = requestPath;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public String getRequestPath() {
		return requestPath;
	}
	
}
