package com.nsn.quick4j.mvc.converter.impl;

import com.nsn.quick4j.mvc.converter.Converter;
import com.nsn.quick4j.mvc.converter.annotation.ConverterType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author donghao
 * @since 1.0
 */
@ConverterType({Date.class})
public class DateConverter implements Converter {

    /**
     * 定义日期格式化对象容器
     */
    private static final List<SimpleDateFormat> DATE_FORMAT_LIST = new ArrayList<SimpleDateFormat>();

    static{
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DATE_FORMAT_LIST.add(datetimeFormat);
        DATE_FORMAT_LIST.add(dateFormat);
        DATE_FORMAT_LIST.add(timeFormat);
    }


    /**
     * 类型转换方法
     *
     * @param value 待转字符串
     * @return 转换后数据
     */
    @Override
    public Object convert(String value) {

        Date date = null;
        /**
         * 遍历格式化对象
         */
        for(SimpleDateFormat dateFormat : DATE_FORMAT_LIST){
            try{
                return dateFormat.parse(value);
            }catch(ParseException e){
                //转换失败不做处理，返回null
            }
        }
        return date;
    }
}
