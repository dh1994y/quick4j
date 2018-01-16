package com.nsn.quick4j.mvc.handler.mapping.impl;

import com.nsn.quick4j.mvc.ControllerHelper;
import com.nsn.quick4j.mvc.bean.Request;
import com.nsn.quick4j.mvc.handler.Handler;
import com.nsn.quick4j.mvc.handler.mapping.HandlerMapping;

import java.util.Map;

/**
 * @author donghao
 * @since 1.0
 */
public class DefaultHandlerMapping implements HandlerMapping{


    /**
     * 通过请求方式和请求路径获取对应处理器
     *
     * @param currentRequestMethod 请求方法（GET、POST等）
     * @param currentRequestPath   请求路径
     * @return 处理指定请求的处理器
     */
    @Override
    public Handler getHandler(String currentRequestMethod, String currentRequestPath) {
        /*
            1、创建Request对象
            2、获取映射容器
            3、根据key（request）查找value（handler）
            4、判断是否找到，找到通过HandlerInvoke执行，否则调用映射异常处理
         */
        Request request = new Request(currentRequestMethod,currentRequestPath);
        Map<Request,Handler> actionMap = ControllerHelper.getActionMap();
        Handler handler = actionMap.get(request);
        // TODO: 2016/4/26 考虑正则匹配，遍历map，判断当前路径正则匹配request正则path
        return handler;
    }

}
