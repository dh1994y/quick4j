package com.nsn.quick4j.mvc.formInjection.impl;

import com.nsn.quick4j.ioc.annotation.Inject;
import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.mvc.converter.Converter;
import com.nsn.quick4j.mvc.converter.ConverterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;

/**
 * @author donghao
 * @since 1.0
 */
public class DefaultFormInjection extends AbstractFormInject{

    /**
     * 日志记录对象
     */
    private static Logger LOGGER = LoggerFactory.getLogger(DefaultFormInjection.class);

    /**
     * 表单数据注入方法
     *
     * @param actionClass 注入的Action的Class对象
     * @param instance    注入的Action的实例对象
     * @param request     请求对象
     */
    @Override
    public void formInject(Class<?> actionClass, Object instance, HttpServletRequest request) {
        /*
        1、获取请求参数名枚举
        2、遍历请求参数列表，在字段列表查询同名字段进行反射赋值
         */
        //获取请求参数名枚举
        Enumeration<String> names = request.getParameterNames();
        //遍历请求参数名枚举
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            //将name按“.”切割  注意转义
            String[] nameArray = name.split("\\.");
            //仅考虑 age  user.name 两种形式
            if (nameArray.length == 1) {
                //一级字段注入 eg:age
                String fieldName = nameArray[0];
                //注入字段
                injectField(actionClass, instance, fieldName, request,false);
            } else if (nameArray.length == 2) {
                //二级字段注入 eg:user.name
                String entityName = nameArray[0];
                String fieldName = name;
                //获取实体字段
                Field entityField = null;
                try{
                    entityField = actionClass.getDeclaredField(entityName);
                }catch(NoSuchFieldException e){
                    //不做处理
                }
                if(entityField == null){
                    continue;
                }
                //获取实体对象
                Object entityObj = ReflectKit.getFieldValue(instance,entityField);
                //判断是否实现模型驱动
                Object model = getModel(actionClass,instance);
                if(model == null || entityObj != model){
                    //若模型驱动为空或该字段非模型驱动对象时注入字段
                    injectField(entityField.getType(),entityObj,fieldName,request,true);
                }
            }
        }
    }


    /**
     * 注入字段数据
     * @param beanClass 字段所属于的类的Class对象
     * @param instance  字段所属于的类的实例
     * @param fieldName 字段名
     * @param request   请求对象
     * @param isEntity  该字段是否为实体类型
     */
    private void injectField(Class<?> beanClass, Object instance, String fieldName, HttpServletRequest request,boolean isEntity) {
        Field field = null;
        //获取请求参数名
        String paramaName = fieldName;
        //若为二级注入，获取二级字段名
        if(isEntity){
            fieldName = fieldName.split("\\.")[1];
        }
        //若前台名为数组名，将数组[]截掉
        if(fieldName.endsWith("[]")){
            fieldName = fieldName.substring(0,fieldName.length()-2);
        }
        try {
            //获取同名字段
            field = beanClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            //无该字段时，不做处理
        }
        //存在该字段时且该字段未标注@Inject注解，进行注入
        if (field != null && !field.isAnnotationPresent(Inject.class)) {
            //获取参数值 若存在同名参数，则拼接使用“,”分割
            String[] paramaValues = request.getParameterValues(paramaName);
            String paramaValue = "";
            for (int i = 0; i < paramaValues.length; i++) {
                paramaValue += paramaValues[i];
                if (i != paramaValues.length - 1) {
                    paramaValue += ",";
                }
            }
            //获取字段类型
            Class<?> fieldType = field.getType();
            //获取指定类型转换器
            Converter converter = ConverterHelper.getConverter(fieldType);
            if (converter == null) {
                //未找到指定类型转换器，则日志记录并抛出异常
                LOGGER.debug("not have matcher Type Converter,targetClass Type:"+fieldType);
                throw new RuntimeException("not have matcher Type Converter,targetClass Type:"+fieldType);
            }
            //通过类型转换器获取转换值
            Object value = converter.convert(paramaValue);
            //通过反射注入
            ReflectKit.setFieldValue(instance, field, value);
        }
    }
}

