package com.nsn.quick4j.kit;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * 用于封装常用反射方法，捕获异常抛出运行时异常
 *
 * @author donghao
 * @since 1.0
 */
public class ReflectKit {

    /**
     * 日志记录对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectKit.class);

    /**
     * 通过反射创建指定类实例对象
     *
     * @param cls 指定类 类对象
     * @return 指定类 对象
     */
    public static <T> T newInstance(Class<T> cls) {
        //声明变量
        T instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            //错误日志记录
            LOGGER.error("new instance failure", e);
            //抛出运行时异常
            throw new RuntimeException("new instance failure!", e);
        }
        return instance;
    }

    /**
     * 通过反射执行指定方法
     *
     * @param obj    被执行方法的类的对象
     * @param method 被执行方法
     * @param args   被执行方法参数
     * @return 被执行方法返回值
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        //声明变量
        Object result;
        try {
            //取消反射安全性检查（访问权限），能显著提高反射效率
            method.setAccessible(true);
            //通过反射调用方法
            result = method.invoke(obj, args);
        } catch (Exception e) {
            //错误日志记录
            LOGGER.error("invoke method failure", e);
            //抛出运行时异常
            throw new RuntimeException("invoke method failure!", e);
        }
        return result;
    }

    /**
     * 通过反射为字段赋值
     *
     * @param obj   被赋值字段的类的对象
     * @param field 被赋值字段
     * @param value 被赋值字段将要设定的值
     */
    public static void setFieldValue(Object obj, Field field, Object value) {
        try {
            //取消反射安全性检查（访问权限）,能显著提高反射效率
            field.setAccessible(true);
            //通过反射为字段赋值
            field.set(obj, value);
        } catch (Exception e) {
            //错误日志记录
            LOGGER.error("set field value failure", e);
            //抛出运行时异常
            throw new RuntimeException("set field("+field.getName()+") value failure!", e);
        }
    }

    /**
     * 通过反射获取字段值
     *
     * @param obj   字段的类的对象
     * @param field 字段
     * @return 字段的值
     */
    public static Object getFieldValue(Object obj, Field field) {
        try {
            //取消反射安全性检查（访问权限）,能显著提高反射效率
            field.setAccessible(true);
            //通过反射获取字段值
            return field.get(obj);
        } catch (Exception e) {
            //错误日志记录
            LOGGER.error("get field value failure", e);
            //抛出运行时异常
            throw new RuntimeException("get field value failure!", e);
        }
    }

    /**
     * 获取字段
     *
     * @param cls
     * @param fieldName
     * @return
     */
    public static Field getField(Class<?> cls, String fieldName) {

        try {
            return cls.getDeclaredField(fieldName);
        } catch (Exception e) {
            //错误日志记录
            LOGGER.error("get field failure", e);
            //抛出运行时异常
            throw new RuntimeException("get field failure!", e);
        }
    }

    /**
     * 获取字段
     *
     * @param cls
     * @param fieldName
     * @return 若存在字段返回对应字段，否则返回null
     */
    public static Field getFieldNoException(Class<?> cls, String fieldName) {

        try {
            return cls.getDeclaredField(fieldName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取指定名称的方法
     * @param cls
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getMethod(Class<?> cls,String methodName,Class<?>... parameterTypes){
        try {
            return cls.getDeclaredMethod(methodName,parameterTypes);
        } catch (Exception e) {
            //错误日志记录
            LOGGER.error("get method failure", e);
            //抛出运行时异常
            throw new RuntimeException("get method failure!", e);
        }
    }

    /**
     * 从字段描述器数组中寻找字段名为给定名称的字段描述器
     * @param fieldName 字段名
     * @param propDes   字段描述器数组
     * @return 字段描述器/null
     */
    public static PropertyDescriptor findPropDes(String fieldName, PropertyDescriptor[] propDes) {
        for (PropertyDescriptor propertyDescriptor : propDes) {
            if (propertyDescriptor.getName().equals(fieldName)) {
                return propertyDescriptor;
            }
        }
        return null;
    }
}
