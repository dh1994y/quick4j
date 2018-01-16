package com.nsn.quick4j.mvc.formInjection;

import javax.servlet.http.HttpServletRequest;

/**
 * @author donghao
 * @since 1.0
 */
public interface FormInjection {

    /**
     * 表单数据注入方法
     * @param actionClass 注入的Action的Class对象
     * @param instance   注入的Action的实例对象
     * @param request   请求对象
     */
    void formInject(Class<?> actionClass, Object instance, HttpServletRequest request);

    Object getModel(Class<?> actionClass, Object instance);
}
