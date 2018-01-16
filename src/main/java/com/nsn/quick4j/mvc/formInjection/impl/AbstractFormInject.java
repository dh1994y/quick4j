package com.nsn.quick4j.mvc.formInjection.impl;

import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.mvc.ModelDriven;
import com.nsn.quick4j.mvc.formInjection.FormInjection;

import java.lang.reflect.Method;

/**
 * 抽象表单注入器
 * @author donghao
 * @since 1.0
 */
public abstract class AbstractFormInject implements FormInjection{

    /**
     * 获取模型驱动对象
     * @return 若存在 返回，否则返回空
     */
    @Override
    public Object getModel(Class<?> actionClass, Object instance){
        //判断是否实现模型驱动
        if(!ModelDriven.class.isAssignableFrom(actionClass)){
            return null;
        }
        //获取驱动对象
        Method method = ReflectKit.getMethod(actionClass,"getModel");
        //获取模型对象
        Object modelObj = ReflectKit.invokeMethod(instance,method);
        return modelObj;
    }
}
