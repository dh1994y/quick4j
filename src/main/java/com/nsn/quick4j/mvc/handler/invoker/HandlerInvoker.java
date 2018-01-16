package com.nsn.quick4j.mvc.handler.invoker;

import com.nsn.quick4j.mvc.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 处理器调度器
 *
 * @author donghao
 * @since 1.0
 */
public interface HandlerInvoker {

    /**
     * 调度执行处理器
     * @param request 请求对象
     * @param response 相应对象
     * @param handler 处理器
     * @throws Exception 异常
     */
    void invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception;
}
