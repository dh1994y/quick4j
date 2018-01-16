package com.nsn.quick4j.orm.handler.impl;

import com.nsn.quick4j.orm.handler.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 将结果集处理成map形式
 * key：字段名
 * value： 值
 * @author donghao
 * @since 1.0
 */
public class MapHandler implements ResultSetHandler<Map<String,Object>>{

    /**
     * map行处理器
     */
    private MapRowProcess mapRowProcess = new MapRowProcess();

    /**
     * 处理方法
     *
     * @param rs 结果集
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        if(!rs.next()){
            return null;
        }
        return mapRowProcess.process(rs);
    }
}
