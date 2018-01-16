package com.nsn.quick4j.orm.handler.impl;

import com.nsn.quick4j.orm.handler.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * 将结果集封装成Bean
 * @author donghao
 * @since 1.0
 */
public class BeanHandler<T> implements ResultSetHandler<T> {

    /**
     * 行处理属性
     */
    private BeanRowProcess<T> beanRowProcess;

    /**
     * 初始化
     * @param clazz
     */
    public BeanHandler(Class<T> clazz){
        beanRowProcess = new BeanRowProcess<T>(clazz);
    }

    /**
     * 处理方法
     *
     * @param rs 结果集
     * @return
     * @throws SQLException
     */
    @Override
    public T handle(ResultSet rs) throws SQLException {
        //判断结果集是否为空
        if(!rs.next()){
            return null;
        }
        //非空调用行处理
        return beanRowProcess.process(rs);
    }
}
