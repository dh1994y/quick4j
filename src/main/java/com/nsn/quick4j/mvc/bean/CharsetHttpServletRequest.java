package com.nsn.quick4j.mvc.bean;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 装饰者模式
 * 字符编码请求对象
 * 用于处理乱码问题
 *
 * @author donghao
 * @since 1.0
 */
public class CharsetHttpServletRequest extends HttpServletRequestWrapper{

	public CharsetHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 将给定字符串转换为utf-8编码
	 * @param value
	 * @return
	 */
	public String covertToUtf_8(String value){
		
		if(value!=null && value.length()>0){
			try {
				if(value.equals(new String(value.getBytes("ISO-8859-1"),"ISO-8859-1"))){
					value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}

	/**
	 * 将给定字符串数组转换为utf-8编码
	 * @param values
	 * @return
	 */
	public String[] covertToUtf_8(String[] values){
		
		if(values==null || values.length==0){
			return values;
		}
		String[] newValues = new String[values.length];
		for (int i=0;i<values.length;i++) {
			newValues[i] = covertToUtf_8(values[i]);
		}
		return newValues;
	}
	
	@Override
	public String getParameter(String name) {
		
		String value = super.getParameter(name);
		return covertToUtf_8(value);
	}
	
	@Override
	public String[] getParameterValues(String name) {
		
		String[] values =  super.getParameterValues(name);
		return covertToUtf_8(values);
	}
	
	@Override
	public Enumeration<String> getParameterNames() {

		final Enumeration<String> enumeration = super.getParameterNames();
		Enumeration<String> newEnumeration = new Enumeration<String>() {
			
			@Override
			public String nextElement() {
				return covertToUtf_8(enumeration.nextElement());
			}
			
			@Override
			public boolean hasMoreElements() {
				return enumeration.hasMoreElements();
			}
		};
		return newEnumeration;
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {

		Map<String, String[]> map = super.getParameterMap();
		Map<String, String[]> newMap = new HashMap<String, String[]>();
		for(Entry<String,String[]> entry:map.entrySet()){
			newMap.put(covertToUtf_8(entry.getKey()),
					covertToUtf_8(entry.getValue()));
		}
		return newMap;
	}
}
