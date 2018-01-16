package com.nsn.quick4j.orm.handler.impl;

import com.nsn.quick4j.orm.handler.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将结果集封装成List<Bean>
 * @author donghao
 * @since 1.0
 */
public class BeanListHandler<T> implements ResultSetHandler<List<T>>{

    /**
     * 行处理属性
     */
    private BeanRowProcess<T> beanRowProcess;

    /**
     * 初始化
     * @param clazz
     */
    public BeanListHandler(Class<T> clazz){
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
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> beanList = new ArrayList<T>();;
        while(rs.next()){
            //创建实例
            T bean = beanRowProcess.process(rs);
            beanList.add(bean);
        }
        return beanList;
    }
}
