package com.nsn.quick4j.mvc.converter;


/**
 * 类型转换器接口
 * @author donghao
 * @since 1.0
 */
public interface Converter {

    /**
     * 类型转换方法
     * @param value  待转字符串
     * @return 转换后数据
     */
    <T> T convert(String value);
}
