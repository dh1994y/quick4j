package com.nsn.quick4j.mvc.viewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 视图解析器接口
 * 用于被实现
 *
 * @author donghao
 * @since 1.0
 */
public interface ViewResolver {

    /**
     * 视图解析
     * @param request  请求对象
     * @param response 响应对象
     * @param actionMethodResult  Action 方法返回值（View：jsp/Result:json数据）
     */
    void resolveView(HttpServletRequest request, HttpServletResponse response,Object actionMethodResult);
}
