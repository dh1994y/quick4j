package com.nsn.quick4j.aop.proxy;

/**
 * 切面接口
 *
 * @author donghao
 * @since 1.0
 */
public interface IAspect {

    /**
     * 执行当前切面方法
     * @param aspectChain 切面链对象
     * @return  切面链上切面方法的返回值
     * @throws Throwable
     */
    Object doAspect(AspectChain aspectChain) throws Throwable;

}
