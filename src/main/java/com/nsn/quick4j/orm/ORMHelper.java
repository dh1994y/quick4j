package com.nsn.quick4j.orm;

import com.nsn.quick4j.dao.DBHelper;
import com.nsn.quick4j.kit.CastKit;
import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.orm.annotation.Extra;
import com.nsn.quick4j.orm.annotation.PKey;
import com.nsn.quick4j.orm.handler.impl.BeanHandler;
import com.nsn.quick4j.orm.handler.impl.BeanListHandler;
import com.nsn.quick4j.orm.handler.impl.MapHandler;
import com.nsn.quick4j.orm.handler.impl.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 目前只提供一种主键映射策略，mysql自增涨
 * 适用数据库：mysql
 * ORM帮助类
 * 封装面向对象的数据库操作方法
 *
 * @author donghao
 * @since 1.0
 */
public class ORMHelper {

    /**
     * 日志记录对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ORMHelper.class);

    /**
     * 面向对象插入操作
     *
     * @param bean 操作类对象
     */
    public static <T> void save(T bean) {
        /*
         * 思路：使用反射内省集合拼凑SQL语句
		 */
        if (bean == null) {
            throw new IllegalArgumentException("save bean is null!");
        }
        //sql语句容器
        StringBuilder sbSql = new StringBuilder();
        //获取反射对象
        Class<?> entityClass = bean.getClass();
        //获取表名
        String tableName = EntityHelper.getTableName(entityClass);
        sbSql.append("insert into ").append(tableName + " ");
        //获取有效列值
        /*
		    存在class不获取
		    注：class由来：Feild是字段，对应的set/get方法是属性，分只读/只写/可读写属性
		 	实际上PropertyDescriptor是根据属性来获取的字段名，由于Object是父类
		 	存在getClass方法，所以会存在class字段！
		 */
        //获取字段数组
        Field[] fields = entityClass.getDeclaredFields();
        //定义三个list：有效列名、？、列值
        List<String> columnNames = new ArrayList<String>();
        List<String> columnMakes = new ArrayList<String>();
        List<Object> columnValues = new ArrayList<Object>();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Extra.class)) {
                Object value = null;
                //主键处理
                if(field.isAnnotationPresent(PKey.class)){
                    //获取主键生成策略
                    PKeyPolicy pKeyPolicy = field.getDeclaredAnnotation(PKey.class).pKeyPolicy();
                    if(pKeyPolicy.equals(PKeyPolicy.AUTO)){
                        //底层mysql自动生成不处理
                        continue;
                    }else if(pKeyPolicy.equals(PKeyPolicy.UUID)){
                        //创建uuid主键值
                        value = UUID.randomUUID().toString();
                    }else if(pKeyPolicy.equals(PKeyPolicy.USER)){
                        //获取用户输入主键
                        value = ReflectKit.getFieldValue(bean, field);
                    }
                }
                //获取字段名
                String fieldName = field.getName();
                //添加对应列名
                columnNames.add(EntityHelper.getColumnName(entityClass, fieldName));
                columnMakes.add("?");
                //反射获取字段值
                if(value == null){
                    value = ReflectKit.getFieldValue(bean, field);
                }
                columnValues.add(value);
            }
        }
        sbSql.append(columnNames.toString().replace('[', '(')
                .replace(']', ')')).append(" values ");
        sbSql.append(columnMakes.toString().replace('[', '(')
                .replace(']', ')'));
        try {
            //调用DBHelper
            DBHelper.executeUpdate(sbSql.toString(),
                    columnValues.toArray());
        } catch (SQLException e) {
            throw new RuntimeException("insert bean error!" + e);
        }
    }


    /**
     * 删除
     *
     * @param entityClass 操作类的反射对象
     * @param id          需删除信息的id
     * @return 返回值标记是否删除  未变更信息返回0
     */
    public static <T> int delete(Class<T> entityClass, Serializable id) {
        int count = 0;
        try {
            //获取表名并创建sql语句
            String tableName = EntityHelper.getTableName(entityClass);
            //获取主键字段名 & 主键名
            String pKey = EntityHelper.getEntityPKey(entityClass);
            String pKeyColumn = EntityHelper.getColumnName(entityClass, pKey);
            String sql = "delete from " + tableName + " where " + pKeyColumn + " = ?";
            //执行Update,并返回是否删除成功
            count = DBHelper.executeUpdate(sql, id);
        } catch (Exception e) {
            throw new RuntimeException("delete bean error!" + e);
        }
        return count;
    }


    /**
     * 查找并返回传入类型的一个对象并已赋值
     *
     * @param entityClass 操作类的反射对象
     * @param id          查找的id
     * @return 返回传入类型的对象
     */
    public static <T> T getBeanById(Class<T> entityClass, Serializable id) {

        //获取表名并创建sql语句
        String tableName = EntityHelper.getTableName(entityClass);
        //获取主键字段名 & 主键名
        String pKey = EntityHelper.getEntityPKey(entityClass);
        String pKeyColumn = EntityHelper.getColumnName(entityClass, pKey);
        String sql = "select from " + tableName + " where " + pKeyColumn + " = ?";
        //调用数据库查找
        ResultSet rs = null;
        try {
            rs = DBHelper.executeQuery(sql, id);
            return new BeanHandler<T>(entityClass).handle(rs);
        } catch (Exception e) {
            throw new RuntimeException("getBean by id error！" + e);
        }
    }

    /**
     * 查询所有
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> List<T> getBeanList(Class<T> entityClass) {

        //获取表名并创建sql语句
        String tableName = EntityHelper.getTableName(entityClass);
        String sql = "select * from " + tableName;
        //调用数据库查找
        ResultSet rs = null;
        try {
            rs = DBHelper.executeQuery(sql);
            return new BeanListHandler<T>(entityClass).handle(rs);
        } catch (Exception e) {
            throw new RuntimeException("getBeanList error！" + e);
        }
    }

    /**
     * 根据条件查询所有
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> List<T> getBeanList(Class<T> entityClass, String sql, Object... param) {

        //调用数据库查找
        ResultSet rs = null;
        try {
            rs = DBHelper.executeQuery(sql,param);
            return new BeanListHandler<T>(entityClass).handle(rs);
        } catch (Exception e) {
            throw new RuntimeException("getBeanList error！" + e);
        }
    }

    /**
     * 根据条件查询一条记录
     * map展示
     *
     * @return
     */
    public static Map<String, Object> getMap(String sql, Object... param) {

        //调用数据库查找
        ResultSet rs = null;
        try {
            rs = DBHelper.executeQuery(sql,param);
            return new MapHandler().handle(rs);
        } catch (Exception e) {
            throw new RuntimeException("getMap error！" + e);
        }
    }

    /**
     * 根据条件查询n条记录
     * maplist展示
     *
     * @return
     */
    public static List<Map<String, Object>> getMapList(String sql, Object... param) {

        //调用数据库查找
        ResultSet rs = null;
        try {
            rs = DBHelper.executeQuery(sql,param);
            return new MapListHandler().handle(rs);
        } catch (Exception e) {
            throw new RuntimeException("getMapList error！" + e);
        }
    }


    /**
     * 更新id对应的信息 使用该操作时需先使用select获取一个对象在修改后更新
     *
     * @param bean 操作类对象
     */
    public static <T> void update(T bean) {
        if (bean == null) {
            throw new IllegalArgumentException("update bean is null!");
        }
        //拼凑sql语句
        StringBuilder sbSql = new StringBuilder();
        Class<?> entityClass = bean.getClass();
        //获取表名
        String tableName = EntityHelper.getTableName(entityClass);
        sbSql.append("update ").append(tableName).append(" set ");
        //获取表有效字段
        //存放set xx=？
        List<String> fieldNames = new ArrayList<String>();
        //存放values
        List<Object> values = new ArrayList<Object>();
        //获取字段数组
        Field[] fields = entityClass.getDeclaredFields();
        //定义三个list：有效列名、？、列值
        for (Field field : fields) {
            if (!field.isAnnotationPresent(PKey.class) &&
                    !field.isAnnotationPresent(Extra.class)) {
                //获取字段名
                String fieldName = field.getName();
                //获取对应列名
                String columnName = EntityHelper.getColumnName(entityClass,fieldName);
                //反射获取字段值
                Object value = ReflectKit.getFieldValue(bean, field);
                fieldNames.add(columnName + "=?");
                values.add(value);
            }
        }
        //获取主键字段名 & 主键列名
        String pKey = EntityHelper.getEntityPKey(entityClass);
        Field pKeyField = ReflectKit.getField(entityClass,pKey);
        Object pKeyValue = ReflectKit.getFieldValue(bean, pKeyField);
        String pKeyColumn = EntityHelper.getColumnName(entityClass, pKey);
        values.add(pKeyValue);
        //继续拼接sql
        sbSql.append(fieldNames.toString().replace("[", "").replace("]", "")).append(" where " + pKeyColumn + "=?");
        try {
            //执行sql
            DBHelper.executeUpdate(sbSql.toString(), values.toArray());
        } catch (Exception e) {
            throw new RuntimeException("update error" + e);
        }
    }

    /**
     * 根据实体Class获取记录总数
     * @param entityClass
     * @return
     */
    public static int recordCount(Class<?> entityClass){
        String sql = "select count(*) as count from "+EntityHelper.getTableName(entityClass);
        Map<String,Object> map = getMap(sql);
        return CastKit.castInt(map.get("count"));
    }
}
