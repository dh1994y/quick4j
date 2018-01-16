package com.nsn.quick4j.mvc.converter.impl;

import com.nsn.quick4j.kit.CastKit;
import com.nsn.quick4j.mvc.converter.Converter;
import com.nsn.quick4j.mvc.converter.annotation.ConverterType;

/**
 * @author donghao
 * @since 1.0
 */
@ConverterType({boolean.class,Boolean.class})
public class BooleanConverter implements Converter{
    /**
     * 类型转换方法
     *
     * @param value 待转字符串
     * @return 转换后数据
     */
    @Override
    public Object convert(String value) {
        return CastKit.castBoolean(value);
    }
}
