package com.nsn.quick4j.orm.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * 解析ResultSet，封装结果集处理器
 * @author donghao
 * @since 1.0
 */
public interface ResultSetHandler<T> {

    /**
     * 处理方法
     * @param rs 结果集
     * @return
     * @throws SQLException
     */
    T handle(ResultSet rs) throws SQLException;
}
