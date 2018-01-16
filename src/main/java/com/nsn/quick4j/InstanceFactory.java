package com.nsn.quick4j;

import com.nsn.quick4j.core.ClassScanner;
import com.nsn.quick4j.core.impl.DefaultClassScanner;
import com.nsn.quick4j.dao.ds.DataSourceFactory;
import com.nsn.quick4j.dao.ds.impl.DefaultDataSourceFactory;
import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.mvc.formInjection.FormInjection;
import com.nsn.quick4j.mvc.formInjection.impl.DefaultFormInjection;
import com.nsn.quick4j.mvc.formInjection.impl.FileUploadFormInjection;
import com.nsn.quick4j.mvc.handler.invoker.HandlerInvoker;
import com.nsn.quick4j.mvc.handler.invoker.impl.DefaultHandlerInvoker;
import com.nsn.quick4j.mvc.handler.mapping.HandlerMapping;
import com.nsn.quick4j.mvc.handler.mapping.impl.DefaultHandlerMapping;
import com.nsn.quick4j.mvc.viewResolver.ViewResolver;
import com.nsn.quick4j.mvc.viewResolver.impl.DefaultViewResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例工厂，用于提供单实例对象
 * 其中提供缓存用于缓存单实例
 *
 * @author donghao
 * @since 1.0
 */
public class InstanceFactory {
    /**
     * 单实例缓存
     * ConcurrentHashMap：并发安全性
     * key:.Class  value：对应实例
     */
    private static final Map<Class<?>, Object>
            cache = new ConcurrentHashMap<Class<?>, Object>();

    /**
     * 通用创建实例方法
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> key) {
        // 若缓存中存在对应的实例，则返回该实例
        if (cache.containsKey(key)) {
            return (T) cache.get(key);
        }
        // 通过反射创建该实现类对应的实例
        T instance = ReflectKit.newInstance(key);
        // 若该实例不为空，则将其放入缓存
        if (instance != null) {
            cache.put(key, instance);
        }
        // 返回该实例
        return instance;
    }

    /**
     * 获取 ClassScanner
     */
    public static ClassScanner getClassScanner() {
        return getInstance(DefaultClassScanner.class);
    }

    /**
     * 获取 HandlerMapping
     */
    public static HandlerMapping getHandlerMapping() {
        return getInstance(DefaultHandlerMapping.class);
    }

    /**
     * 获取 HandlerInvoker
     */
    public static HandlerInvoker getHandlerInvoker() {
        return getInstance(DefaultHandlerInvoker.class);
    }

    /**
     * 获取FileUploadFormInjection
     */
    public static FormInjection getFileUploadFormInjection() {
        return getInstance(FileUploadFormInjection.class);
    }

    /**
     * 获取DefaultFormInjection
     */
    public static FormInjection getDefaultFormInjection() {
        return getInstance(DefaultFormInjection.class);
    }

    /**
     * 获取 ViewResolver
     */
    public static ViewResolver getViewResolver() {
        return getInstance(DefaultViewResolver.class);
    }

    /**
     * 获取 DataSourceFactory
     */
    public static DataSourceFactory getDataSourceFactory() {
        return getInstance(DefaultDataSourceFactory.class);
    }

}
