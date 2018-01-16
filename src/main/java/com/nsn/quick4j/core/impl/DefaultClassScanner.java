package com.nsn.quick4j.core.impl;

import com.nsn.quick4j.core.ClassScanner;
import com.nsn.quick4j.core.impl.support.AnnotationClassTemplate;
import com.nsn.quick4j.core.impl.support.ClassTemplate;
import com.nsn.quick4j.core.impl.support.SuperClassTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * 类扫描器默认实现类
 * @author donghao
 * @since 1.0
 */
public class DefaultClassScanner implements ClassScanner{

    /**
     * 日志记录属性
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClassScanner.class);

    /**
     * 获取指定包名中的所有类
     * @param packageName 指定包名
     * @return 所有类Class对象Set集合
     */
    @Override
    public List<Class<?>> getClassList(String packageName) {
        LOGGER.info("[INFO] ClassHelper：加载"+packageName+"包下所有类");
        return new ClassTemplate(packageName){
            @Override
            public boolean checkAddClass(Class<?> cls){
               return true;
            }
        }.getClassList();
    }

    /**
     * 获取指定报名中指定注解的相关类
     * @param packageName 指定包名
     * @param annotationClass 指定注解
     * @return 相关类Class对象List集合
     */
    @Override
    public List<Class<?>> getClassListByAnnotation(String packageName,
                                                   Class<? extends Annotation> annotationClass) {
        LOGGER.info("[INFO] ClassHelper：加载"+packageName+"包下所有"+annotationClass.getSimpleName()+"类");
        return new AnnotationClassTemplate(packageName,annotationClass){
            @Override
            public boolean checkAddClass(Class<?> cls){
                return cls.isAnnotationPresent(annotationClass);
            }
        }.getClassList();
    }

    /**
     * 获取给定List中指定注解的相关类
     * @param classList 给定List
     * @param annotationClass 指定注解
     * @return 相关类Class对象Set集合
     */
    @Override
    public List<Class<?>> getClassListByAnnotation(List<Class<?>> classList, Class<? extends Annotation> annotationClass) {
        List<Class<?>> AnnotationClassList = new ArrayList<Class<?>>();
        for(Class<?> cls : classList){
            if(cls.isAnnotationPresent(annotationClass)){
                AnnotationClassList.add(cls);
            }
        }
        return AnnotationClassList;
    }

    /**
     * 获取指定报名下指定父类或接口的相关类
     * @param packageName 指定包名
     * @param superClass 指定父类或接口
     * @return 相关类Class对象Set集合
     */
    @Override
    public List<Class<?>> getClassListBySuper(String packageName, Class<?> superClass) {
        LOGGER.info("[INFO] ClassHelper：加载"+packageName+"包下所有继承/实现"+superClass.getSimpleName()+"的类");
        return new SuperClassTemplate(packageName,superClass){
            @Override
            public boolean checkAddClass(Class<?> cls){
                return superClass.isAssignableFrom(cls) && !superClass.equals(cls);
            }
        }.getClassList();
    }

    /**
     * 获取给定List中指定父类或接口的相关类
     * @param classList 给定List
     * @param superClass 指定父类或接口
     * @return 相关类Class对象Set集合
     */
    @Override
    public List<Class<?>> getClassListBySuper(List<Class<?>> classList, Class<?> superClass) {
        List<Class<?>> superClassList = new ArrayList<Class<?>>();
        for(Class<?> cls : classList){
            if(superClass.isAssignableFrom(cls) && !superClass.equals(cls)){
                superClassList.add(cls);
            }
        }
        return superClassList;
    }
}
