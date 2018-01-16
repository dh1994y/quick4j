package com.nsn.quick4j.core;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 *
 * 类扫描器接口
 *
 * @author donghao
 * @since 1.0
 */
public interface ClassScanner {

    /**
     * 获取指定包名中的所有类
     * @param packageName 指定包名
     * @return 所有类Class对象Set集合
     */
    List<Class<?>> getClassList(String packageName);

    /**
     * 获取指定报名中指定注解的相关类
     * @param packageName 指定包名
     * @param annotationClass 指定注解
     * @return 相关类Class对象List集合
     */
    List<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass);

    /**
     * 获取给定List中指定注解的相关类
     * @param classList 给定List
     * @param annotationClass 指定注解
     * @return 相关类Class对象Set集合
     */
    List<Class<?>> getClassListByAnnotation(List<Class<?>> classList, Class<? extends Annotation> annotationClass);

    /**
     * 获取指定报名下指定父类或接口的相关类
     * @param packageName 指定包名
     * @param superClass 指定父类或接口
     * @return 相关类Class对象Set集合
     */
    List<Class<?>> getClassListBySuper(String packageName,Class<?> superClass);

    /**
     * 获取给定List中指定父类或接口的相关类
     * @param classList 给定List
     * @param superClass 指定父类或接口
     * @return 相关类Class对象Set集合
     */
    List<Class<?>> getClassListBySuper(List<Class<?>> classList, Class<?> superClass);
}
