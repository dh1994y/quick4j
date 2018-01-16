package com.nsn.quick4j.mvc.converter;

import com.nsn.quick4j.InstanceFactory;
import com.nsn.quick4j.core.ClassHelper;
import com.nsn.quick4j.core.ClassScanner;
import com.nsn.quick4j.core.error.InitializationError;
import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.mvc.converter.annotation.ConverterType;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型转换器帮助类
 * 初始化类型转换器，以及根据类型获取对应类型转换器
 *
 * @author donghao
 * @since 1.0
 */
public class ConverterHelper {

    /**
     * 日志记录属性
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConverterHelper.class);

    /**
     * 定义类扫描器
     */
    private static final ClassScanner CLASS_SCANNER = InstanceFactory.getClassScanner();

    /**
     * 类型转换器容器
     * key:目标类型
     * value：对应转换器实例
     */
    private static final Map<Class<?>, Converter> CONVERTER_MAP = new HashMap<Class<?>, Converter>();

    /**
     * 初始化
     */
    static {
        init();
    }

    public static void init(){
        try {
            LOGGER.info("[INFO] ConverterHelper：初始化类型转换器容器：CONVERTER_MAP 开始");
            //获取Quick4j预定义类型转换器
            List<Class<?>> converList = CLASS_SCANNER.getClassListBySuper("com.nsn.quick4j.mvc.converter",Converter.class);
            addConverter(converList);
            //获取用户类型转换器列表
            List<Class<?>> userConverterList = ClassHelper.getClassListBySuper(Converter.class);
            addConverter(userConverterList);
            LOGGER.info("[INFO] ConverterHelper：初始化类型转换器容器：CONVERTER_MAP 结束");
        } catch (Exception e) {
            //记录错误日志
            LOGGER.error("Initialization ConverterHelper error！");
            //抛出初始化错误
            throw new InitializationError("Initialization ConverterHelper error！",e);
        }
    }

    /**
     * 将列表中转换器添加到容器
     * @param converterList
     */
    private static void addConverter(List<Class<?>> converterList){
        //遍历初始化并加入容器
        for (Class<?> converterClass : converterList) {
            //获取目标类型
            ConverterType converterType = converterClass.getAnnotation(ConverterType.class);
            Class<?>[] targetClassArray = converterType.value();
            //创建类型转换器实例
            Converter instance = (Converter) ReflectKit.newInstance(converterClass);
            //加入CONVERTER_MAP
            for(Class<?> targetClass : targetClassArray){
                CONVERTER_MAP.put(targetClass, instance);
            }
        }
    }


    /**
     * 获取指定类型转换器
     * @param targetClass
     * @return
     */
    public static Converter getConverter(Class<?> targetClass){
        return CONVERTER_MAP.get(targetClass);
    }
}
