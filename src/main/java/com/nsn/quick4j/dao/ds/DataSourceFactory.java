package com.nsn.quick4j.dao.ds;

import javax.sql.DataSource;

/**
 *
 * 数据库连接池工厂接口
 * 提供获取数据库连接池方法
 * @author donghao
 * @since 1.0
 */
public interface DataSourceFactory <T extends DataSource>{

    /**
     * 获取数据库连接池
     * @return 数据库连接池
     */
    T getDataSource();
}
