package com.nsn.quick4j.mvc.handler.mapping;

import com.nsn.quick4j.mvc.handler.Handler;

/**
 *
 * 处理器映射
 *
 * @author donghao
 * @since 1.0
 */
public interface HandlerMapping {

    /**
     * 通过请求方式和请求路径获取对应处理器
     * @param currentRequestMethod 请求方法（GET、POST等）
     * @param currentRequestPath 请求路径
     * @return 处理指定请求的处理器
     */
    Handler getHandler(String currentRequestMethod,String currentRequestPath);

}
