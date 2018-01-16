package com.nsn.quick4j.mvc;

import com.nsn.quick4j.mvc.bean.CharsetHttpServletRequest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;


/**
 * 前端字符编码过滤器
 * 用于处理乱码问题
 *
 * @author donghao
 * @since 1.0
 */
@WebFilter("/*")
public class CharSetFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//设置响应头信息
		response.setContentType("text/html;charset=utf-8");
		//创建字符编码request对象
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		CharsetHttpServletRequest charsetHttpServletRequest = new CharsetHttpServletRequest(httpServletRequest);
		//放行新的request、response
		chain.doFilter(charsetHttpServletRequest, response);
	}

	@Override
	public void destroy() {

	}
}
