package com.nsn.quick4j.orm.handler.impl;

import com.nsn.quick4j.kit.StringKit;
import com.nsn.quick4j.orm.EntityHelper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 行处理工具
 * 处理map
 * @author donghao
 * @since 1.0
 */
public class MapRowProcess {

    public Map<String,Object> process(ResultSet rs) throws SQLException {
        //创建map
        Map<String,Object> map = new HashMap<String, Object>();
        //获取元数据
        ResultSetMetaData metaData = rs.getMetaData();
        //遍历并赋值
        for(int i=1;i<=metaData.getColumnCount();i++) {
            //获取列名 & 表名 & Class
            String columnName = metaData.getColumnName(i);
            String fieldName = columnName;
            String tableName = metaData.getTableName(i);
            //若存在表
            if(StringKit.isNotEmpty(tableName)){
                Class<?> entityClass = EntityHelper.getEntityClass(tableName);
                //获取对应字段名
                fieldName = EntityHelper.getFieldName(entityClass, columnName);
            }
            //获取列值
            Object value = rs.getObject(columnName);
            //添加到map
            map.put(fieldName,value);
        }
        return map;
    }
}
