package com.nsn.quick4j.dao.impl;

import com.nsn.quick4j.dao.BaseDao;
import com.nsn.quick4j.orm.ORMHelper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author donghao
 * @since 1.0
 */
public class BaseDaoImpl<T> implements BaseDao<T>{

    private Class<T> entityClass;

    /**
     * 仅供继承使用，外部不可new
     */
    protected BaseDaoImpl(){
        // 获取参数化类型
        ParameterizedType paramType = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        Type[] argTypes = paramType.getActualTypeArguments();
        entityClass = (Class<T>) argTypes[0];
    }
    /**
     * 插入bean
     *
     * @param bean 操作类对象
     */
    @Override
    public void save(T bean) {
        ORMHelper.save(bean);
    }

    /**
     * 插入List
     *
     * @param beanList 操作类对象
     */
    @Override
    public void saveList(List<T> beanList) {
        for(T bean : beanList){
            save(bean);
        }
    }

    /**
     * 更新id对应的信息 使用该操作时需先使用select获取一个对象在修改后更新
     *
     * @param bean 操作类对象
     */
    @Override
    public void update(T bean) {
        ORMHelper.update(bean);
    }

    /**
     * 更新List
     *
     * @param beanList 操作类对象
     */
    @Override
    public void updateList(List<T> beanList) {
        for(T bean : beanList){
            update(bean);
        }
    }

    /**
     * 删除
     *
     * @param id 需删除信息的id
     * @return 返回值标记是否删除  未变更信息返回0
     */
    @Override
    public int delete(Serializable id) {
        return ORMHelper.delete(entityClass,id);
    }

    /**
     * 删除一组
     *
     * @param ids 需删除信息的ids
     * @return 返回值标记是否删除  未变更信息返回0
     */
    @Override
    public void deleteByIds(Serializable... ids) {
        for(Serializable id : ids){
            delete(id);
        }
    }

    /**
     * 根据id查询
     *
     * @param id 查找的id
     * @return 返回传入类型的对象
     */
    @Override
    public T getBeanById(Serializable id) {
        return ORMHelper.getBeanById(entityClass,id);
    }

    /**
     * 根据ids查询所有
     *
     * @param ids 查找的id
     * @return 返回传入类型的对象
     */
    @Override
    public List<T> getBeanListByIds(Serializable... ids) {
        List<T> beanList = new ArrayList<T>();
        for(Serializable id : ids){
            beanList.add(getBeanById(id));
        }
        return beanList;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<T> getBeanList() {
        return ORMHelper.getBeanList(entityClass);
    }

    /**
     * 根据条件查询所有
     *
     * @param sql
     * @param param
     * @return
     */
    @Override
    public List<T> getBeanList(String sql, Object... param) {
        return ORMHelper.getBeanList(entityClass,sql,param);
    }

    /**
     * 根据条件查询一条记录
     * map展示
     *
     * @param sql
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> getMap(String sql, Object... param) {
        return ORMHelper.getMap(sql,param);
    }

    /**
     * 根据条件查询n条记录
     * maplist展示
     *
     * @param sql
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> getMapList(String sql, Object... param) {
        return ORMHelper.getMapList(sql,param);
    }

    @Override
    public int recordCount() {
        return ORMHelper.recordCount(entityClass);
    }
}
