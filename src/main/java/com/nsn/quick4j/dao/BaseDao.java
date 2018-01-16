package com.nsn.quick4j.dao;

import com.nsn.quick4j.orm.ORMHelper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseDao接口
 * 定义通用数据库访问方法
 * @author donghao
 * @since 1.0
 */
public interface BaseDao<T> {

    /**
     * 插入bean
     * @param bean 操作类对象
     */
    public void save(T bean);

    /**
     * 插入List
     * @param beanList 操作类对象
     */
    public void saveList(List<T> beanList);

    /**
     * 更新id对应的信息 使用该操作时需先使用select获取一个对象在修改后更新
     * @param bean 操作类对象
     */
    public void update(T bean);

    /**
     * 更新List
     * @param beanList 操作类对象
     */
    public void updateList(List<T> beanList);

    /**
     * 删除
     * @param id    需删除信息的id
     * @return 返回值标记是否删除  未变更信息返回0
     */
    public int delete(Serializable id);

    /**
     * 删除一组
     * @param ids    需删除信息的ids
     * @return 返回值标记是否删除  未变更信息返回0
     */
    public void deleteByIds(Serializable... ids);


    /**
     * 根据id查询
     * @param id    查找的id
     * @return      返回传入类型的对象
     */
    public T getBeanById(Serializable id);

    /**
     * 根据ids查询所有
     * @param ids    查找的id
     * @return      返回传入类型的对象
     */
    public List<T> getBeanListByIds(Serializable... ids);

    /**
     * 查询所有
     * @return
     */
    public List<T> getBeanList();

    /**
     * 根据条件查询所有
     * @return
     */
    public List<T> getBeanList(String sql,Object... param);

    /**
     * 根据条件查询一条记录
     * map展示
     * @return
     */
    public Map<String,Object> getMap(String sql, Object... param);

    /**
     * 根据条件查询n条记录
     * maplist展示
     * @return
     */
    public List<Map<String,Object>> getMapList(String sql, Object... param);

    /**
     * 返回记录总数
     * @return
     */
    public int recordCount();
}
