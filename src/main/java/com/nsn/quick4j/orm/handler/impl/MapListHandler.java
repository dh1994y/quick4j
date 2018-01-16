package com.nsn.quick4j.orm.handler.impl;

import com.nsn.quick4j.orm.handler.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 将结果集 封装成List<Map<String,Object>>
 * @author donghao
 * @since 1.0
 */
public class MapListHandler implements ResultSetHandler<List<Map<String,Object>>>{

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
    public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
        //创建list
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        while(rs.next()){
            Map<String,Object> map = mapRowProcess.process(rs);
            mapList.add(map);
        }
        return mapList;
    }
}
