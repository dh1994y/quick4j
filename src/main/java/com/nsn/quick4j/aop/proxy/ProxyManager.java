package com.nsn.quick4j.aop.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理器
 * 通过封装CGlib类库api创建代理对象
 * CGLIB具有方法级别的拦截器
 * @author donghao
 * @since 1.0
 */
public class ProxyManager {

    /**
     * 创建代理对象
     * @param targetClass 目标类Class
     * @param aspectList   切面类列表
     * @return
     */

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<IAspect> aspectList) {
        return (T)Enhancer.create(targetClass, new MethodInterceptor() {
            /**
             * 方法拦截器
             * @param targetObject 目标对象
             * @param targetMethod 目标方法
             * @param methodParams 方法参数
             * @param methodProxy  方法代理
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new AspectChain(targetClass, targetObject, targetMethod, methodProxy, methodParams, aspectList).doAspectChain();
            }
        });
    }
}
