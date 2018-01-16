package com.nsn.quick4j.dao;

import com.nsn.quick4j.InstanceFactory;
import com.nsn.quick4j.core.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库操作帮助类
 *
 * @author donghao
 * @since 1.0
 */
public class DBHelper {

    /**
     * 日志记录对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DBHelper.class);

    /**
     * 数据库连接容器，保证每个线程拥有独自的连接
     */
    private static final ThreadLocal<Connection> CONN_CONTAINER = new ThreadLocal<Connection>();

    /**
     * 数据库连接池工厂
     */
    private static final DataSource DATA_SOURCE = InstanceFactory.getDataSourceFactory().getDataSource();


    /**
     * 获取当前线程数据库连接
     * @return currentThread conn
     */
    public static Connection getConnection(){
        //从容器中获取当前线程对应的conn
        Connection conn = CONN_CONTAINER.get();
        //若不存在，则从数据库连接池中获取并加入容器
        if(conn == null){
            try{
                conn = DATA_SOURCE.getConnection();
                CONN_CONTAINER.set(conn);
            }catch(SQLException e){
                //错误日志记录
                LOGGER.error("get database connection error!",e);
                //抛出运行时异常
                throw new RuntimeException("get database connection error!",e);
            }
        }
        return conn;
    }

    /**
     * 关闭当前线程数据库连接
     */
    public void closeConnection(){
        //从容器中获取当前线程对应的conn
        Connection conn = CONN_CONTAINER.get();
        //若不存在，则从数据库连接池中获取并加入容器
        if(conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                //错误日志记录
                LOGGER.error("closeConnection error!",e);
                //抛出运行时异常
                throw new RuntimeException("closeConnection error!",e);
            }
        }
    }

    /**
     * 开始事务
     */
    public static void beginTransaction(){
        //获取当前线程连接
        Connection conn = getConnection();
        if(conn != null){
            try{
                //关闭事务自动提交
                conn.setAutoCommit(false);
            }catch(SQLException e){
                //错误日志记录
                LOGGER.error("beginTransaction error!",e);
                //抛出运行时异常
                throw new RuntimeException("beginTransaction error!",e);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction(){
        //获取当前线程连接
        Connection conn = getConnection();
        if(conn != null){
            try{
                //提交事务
                conn.commit();
                //关闭连接
                conn.close();
            }catch(SQLException e){
                //错误日志记录
                LOGGER.error("commitTransaction error!",e);
                //抛出运行时异常
                throw new RuntimeException("commitTransaction error!",e);
            }finally {
                //将当前线程连接移除
                CONN_CONTAINER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        //获取当前线程连接
        Connection conn = getConnection();
        if(conn != null){
            try{
                //提交事务
                conn.rollback();
                //关闭连接
                conn.close();
            }catch(SQLException e){
                //错误日志记录
                LOGGER.error("rollbackTransaction error!",e);
                //抛出运行时异常
                throw new RuntimeException("rollbackTransaction error!",e);
            }finally {
                //将当前线程连接移除
                CONN_CONTAINER.remove();
            }
        }
    }

    /**
     * 安静的关闭资源
     * @param auto 要关闭的资源：resultSet statement connection
     */
    public static void closeQuietly(AutoCloseable  auto)
    {
        if(auto != null)
        {
            try
            {
                auto.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    /**
     * 更新数据表
     * @param sql  更新语句
     * @param parameters  需更新的参数
     * @return   影响了几条信息
     * @throws SQLException  抛出异常
     */
    public static int executeUpdate(String sql,Object... parameters) throws SQLException {

        //打印sql语句
        if(ConfigHelper.isShowSql()){
            System.out.println(sql);
        }
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = getConnection();
            prep = conn.prepareStatement(sql);
            for(int i=1;i<=parameters.length;i++) {
                prep.setObject(i,parameters[i-1]);
            }
            return prep.executeUpdate();
        }//不要捕获异常抛出去
        finally {
            closeQuietly(prep);
            //不要关闭连接，可能还要用！
        }
    }

    /**
     * 查询数据表
     * @param sql  查询语句
     * @param parameters 需查询的参数
     * @return  查询结果集
     * @throws SQLException 抛出异常
     */
    public static ResultSet executeQuery(String sql, Object... parameters) throws SQLException {
        //打印sql语句
        if(ConfigHelper.isShowSql()){
            System.out.println(sql);
        }
        //获取当前线程连接
        Connection conn = getConnection();
        PreparedStatement prep = conn.prepareStatement(sql);
        for(int i=0;i<parameters.length;i++){
            prep.setObject(i+1,parameters[i]);
        }
        //此处不能关闭prep，resultSet依赖于prep！
        return prep.executeQuery();
    }
}
