package com.nsn.quick4j.dao.ds.impl;

import com.nsn.quick4j.core.ConfigHelper;
import com.nsn.quick4j.dao.ds.DataSourceFactory;
import org.apache.commons.dbcp2.BasicDataSource;


/**
 *
 * 默认数据库连接池dbcp
 *
 * @author donghao
 * @since 1.0
 */
public class DefaultDataSourceFactory implements DataSourceFactory<BasicDataSource>{

    /*
    定义相关字段
     */
    private String driver = ConfigHelper.getJdbcDriver();
    private String username = ConfigHelper.getJdbcUsername();
    private String password = ConfigHelper.getJdbcPassword();
    private String url = ConfigHelper.getJdbcUrl();

    private final BasicDataSource ds = new BasicDataSource();

    public DefaultDataSourceFactory(){
        /**
         * 初始化相关配置
         */
        setDriver(ds,driver);
        setUsername(ds,username);
        setPassword(ds,password);
        setUrl(ds,url);
    }


    /**
     * 获取数据库连接池
     *
     * @return 数据库连接池
     */
    @Override
    public BasicDataSource getDataSource() {
        return ds;
    }

    /**
     * 设置驱动
     * @param ds
     * @param driver
     */
    public void setDriver(BasicDataSource ds,String driver){
        ds.setDriverClassName(driver);
    }

    /**
     * 设置用户名
     * @param ds
     * @param username
     */
    public void setUsername(BasicDataSource ds,String username){
        ds.setUsername(username);
    }

    /**
     * 设置密码
     * @param ds
     * @param password
     */
    public void setPassword(BasicDataSource ds,String password){
        ds.setPassword(password);
    }

    /**
     * 设置url
     * @param ds
     * @param url
     */
    public void setUrl(BasicDataSource ds,String url){
        ds.setUrl(url);
    }
}
