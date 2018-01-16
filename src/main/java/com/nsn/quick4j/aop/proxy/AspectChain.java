package com.nsn.quick4j.aop.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 切面链
 *
 * @author donghao
 * @since 1.0
 */
public class AspectChain {

    /**
     * 目标类Class对象
     */
    private final Class<?> targetClass;
    /**
     * 目标类对象
     */
    private final Object targetObject;
    /**
     * 目标类方法
     */
    private final Method targetMethod;
    /**
     * 目标类方法代理
     */
    private final MethodProxy methodProxy;
    /**
     * 目标类方法运行传入参数
     */
    private final Object[] methodParams;

    /**
     * 代理集合
     */
    private List<IAspect> aspectList;

    /**
     * 代理链执行下标
     */
    private int aspectIndex = 0;

    public AspectChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<IAspect> aspectList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.aspectList = aspectList;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    /**
     * 执行切面链
     *
     * @return 被执行切面方法的返回值
     * @throws Throwable
     */
    public Object doAspectChain() throws Throwable {
        //声明方法结果
        Object methodResult = null;
        //执行链式切面
        if(aspectIndex < aspectList.size()){
            methodResult = aspectList.get(aspectIndex++).doAspect(this);
        }else{
            methodResult = methodProxy.invokeSuper(targetObject,methodParams);
        }
        return methodResult;
    }
}
