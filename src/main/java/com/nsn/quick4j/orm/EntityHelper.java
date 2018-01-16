package com.nsn.quick4j.orm;

import com.nsn.quick4j.core.ClassHelper;
import com.nsn.quick4j.core.error.InitializationError;
import com.nsn.quick4j.kit.StringKit;
import com.nsn.quick4j.orm.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体-数据库映射帮助类
 *
 * @author donghao
 * @since 1.0
 */
public class EntityHelper {

    /**
     * 日志记录对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Entity.class);

    /**
     * 实体类和表名映射容器
     * key：实体类Class对象
     * value：表名
     */
    private static final Map<Class<?>, String> ENTITY_TABLE_MAP = new HashMap<Class<?>, String>();

    /**
     * 表名和实体类映射容器
     * key：表名
     * value：实体类Class对象
     */
    private static final Map<String, Class<?>> TABLE_ENTITY__MAP = new HashMap<String, Class<?>>();

    /**
     * 实体类和主键字段名映射容器
     * key：实体类Class对象
     * value：主键字段名
     */
    private static final Map<Class<?>, String> ENTITY_PK_MAP = new HashMap<Class<?>, String>();

    /**
     * 实体类和（表字段与实体字段映射）映射容器
     * key：实体类Class对象
     * value： 表字段与实体字段映射(key:columnName,value:fieldName)
     */
    private static final Map<Class<?>, Map<String, String>> ENTITY_COLUMN_MAP = new HashMap<Class<?>, Map<String, String>>();
    /**
     * 实体类和（实体字段与表字段映射）映射容器
     * key：实体类Class对象
     * value： 实体字段与表字段映射容器(key:fieldName,value:columnName)
     */
    private static final Map<Class<?>, Map<String, String>> ENTITY_FIELD_MAP = new HashMap<Class<?>, Map<String, String>>();

    /**
     * 初始化容器
     */
    static {
        init();
    }

    public static void init(){
        try {
            LOGGER.info("[INFO] EntityHelper：初始化数据库映射容器：ENTITY_TABLE_MAP and ENTITY_FIELD_MAP 开始");
            //获取标注@Entity注解的所有类
            List<Class<?>> entityClassList = ClassHelper.getClassListByAnnotation(Entity.class);
            //遍历list
            for (Class<?> entityClass : entityClassList) {
                //获取表名添加到 ENTITY_TABLE_MAP 容器
                Table tableAnnotation = entityClass.getAnnotation(Table.class);
                //若未标注table注解，默认表名与类名一致
                String tableName = entityClass.getSimpleName().toLowerCase();
                if (tableAnnotation != null) {
                    //若标注table注解
                    String tableAnnotationValue = tableAnnotation.value();
                    if (StringKit.isNotEmpty(tableAnnotationValue)) {
                        //若非空，赋值，否则等于实体名
                        tableName = tableAnnotationValue.toLowerCase();
                    }
                }
                //加入容器
                if(TABLE_ENTITY__MAP.containsKey(tableName)){
                    throw new RuntimeException("tableName{"+tableName+"} has repeat!");
                }
                TABLE_ENTITY__MAP.put(tableName,entityClass);
                ENTITY_TABLE_MAP.put(entityClass, tableName);
                //解析该类字段
                Field[] fields = entityClass.getDeclaredFields();
                //初始化fieldMap&columnMap
                Map<String, String> fieldMap = new HashMap<String, String>();
                Map<String, String> columnMap = new HashMap<String, String>();
                for (Field field : fields) {
                    //判断是否是额外字段，若是，跳过
                    if (field.isAnnotationPresent(Extra.class)) {
                        continue;
                    }
                    //判断是否标注@Column注解,默认表字段名和类字段名一致
                    String fieldName = field.getName();
                    String tableColumnName = fieldName;
                    if (field.isAnnotationPresent(Column.class)) {
                        //若标注column注解
                        String columnAnnotationValue = field.getAnnotation(Column.class).value();
                        if (StringKit.isNotEmpty(tableColumnName)) {
                            //若非空，赋值，空则同名
                            tableColumnName = columnAnnotationValue;
                        }
                    }
                    //判断是否为主键
                    if (field.isAnnotationPresent(PKey.class)) {
                        //若标注PKey注解
                        String pKeyAnnotationValue = field.getAnnotation(PKey.class).value();
                        if (StringKit.isNotEmpty(pKeyAnnotationValue)) {
                            //若非空，赋值，空则同名
                            tableColumnName = pKeyAnnotationValue;
                        }
                        //避免多主键
                        if(ENTITY_PK_MAP.containsKey(entityClass)){
                            throw new RuntimeException("this entityClass already has a pKey:"+ENTITY_PK_MAP.get(entityClass));
                        }
                        ENTITY_PK_MAP.put(entityClass,fieldName);
                    }
                    fieldMap.put(fieldName, tableColumnName);
                    columnMap.put(tableColumnName,fieldName);
                }
                if(!ENTITY_PK_MAP.containsKey(entityClass)){
                    throw new RuntimeException("must has a pKey:"+entityClass);
                }
                //添加映射
                ENTITY_FIELD_MAP.put(entityClass, fieldMap);
                ENTITY_COLUMN_MAP.put(entityClass, columnMap);
            }
            LOGGER.info("[INFO] EntityHelper：初始化数据库映射容器：ENTITY_TABLE_MAP and ENTITY_FIELD_MAP 结束");
        } catch (Exception e) {
            //记录错误日志
            LOGGER.error("Initialization EntityHelper error！", e);
            //抛出初始化错误
            throw new InitializationError("Initialization EntityHelper error！", e);
        }
    }

    /**
     * 获取Class与类字段和表列映射的映射关系集合
     * @return
     */
    public static Map<Class<?>, Map<String, String>> getEntityTableMap(){
        return ENTITY_FIELD_MAP;
    }

    /**
     * 通过实体类Class对象获取对应表名
     *
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass) {
        return ENTITY_TABLE_MAP.get(entityClass);
    }

    /**
     * 通过表名获取对应实体类Class对象
     *
     * @param tableName
     * @return
     */
    public static Class<?> getEntityClass(String tableName) {
        return TABLE_ENTITY__MAP.get(tableName);
    }

    /**
     * 通过实体Class获取对应主键字段名
     *
     * @param entityClass
     * @return
     */
    public static String getEntityPKey(Class<?> entityClass) {
        return ENTITY_PK_MAP.get(entityClass);
    }

    /**
     * 获取数据库列名
     *
     * @param entityClass 指定实体类Class
     * @param fieldName   实体类字段名
     * @return
     */
    public static String getColumnName(Class<?> entityClass, String fieldName) {
        Map<String, String> fieldMap = getFieldMap(entityClass);
        return fieldMap.get(fieldName);
    }

    /**
     * 获取字段名
     *
     * @param entityClass 指定实体类Class
     * @param columnName   实体类字段名
     * @return
     */
    public static String getFieldName(Class<?> entityClass, String columnName) {
        Map<String, String> columnMap = getColumnMap(entityClass);
        return columnMap.get(columnName);
    }

    /**
     * 获取指定实体类  字段与列映射  key:field
     * @param entityClass
     * @return
     */
    public static Map<String, String> getFieldMap(Class<?> entityClass) {
        return ENTITY_FIELD_MAP.get(entityClass);
    }

    /**
     * 获取指定实体类  列与字段映射   key:column
     * @param entityClass
     * @return
     */
    public static Map<String, String> getColumnMap(Class<?> entityClass) {
        return ENTITY_COLUMN_MAP.get(entityClass);
    }
}
