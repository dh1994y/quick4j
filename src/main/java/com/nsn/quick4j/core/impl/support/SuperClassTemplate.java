package com.nsn.quick4j.core.impl.support;

/**
 *
 * 用于获取父类或接口的模板类
 *
 * @author donghao
 * @since 1.0
 */
public abstract class SuperClassTemplate extends ClassTemplate{

    protected final Class<?> superClass;

    protected SuperClassTemplate(String packageName,Class<?> superClass){
        super(packageName);
        this.superClass = superClass;
    }
}
