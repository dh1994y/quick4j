package com.nsn.quick4j.orm.handler.impl;

import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.orm.EntityHelper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 行处理工具类
 * 处理Bean
 * @author donghao
 * @since 1.0
 */
public class BeanRowProcess<T> {

    /**
     * 描述泛型T
     */
    private Class<T> clazz;

    public BeanRowProcess(Class<T> clazz){
        this.clazz = clazz;
    }

    /**
     * bean类型行处理
     * @param rs 结果集
     * @return bean对象
     * @throws SQLException
     */
    public T process(ResultSet rs) throws SQLException{
        //创建Bean实例
        T bean = ReflectKit.newInstance(clazz);
        //获取元数据
        ResultSetMetaData metaData = rs.getMetaData();
        //遍历并赋值
        for(int i=1;i<=metaData.getColumnCount();i++) {
            //获取列名
            String columnName = metaData.getColumnName(i);
            //获取对应字段名&字段
            String fieldName = EntityHelper.getFieldName(clazz, columnName);
            Field field = ReflectKit.getField(clazz, fieldName);
            //获取列值
            Object value = rs.getObject(columnName);
            //反射注入
            ReflectKit.setFieldValue(bean, field, value);
        }
        return bean;
    }
}
