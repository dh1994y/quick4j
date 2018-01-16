package com.nsn.quick4j.aop;

import com.nsn.quick4j.InstanceFactory;
import com.nsn.quick4j.aop.annotation.Aspect;
import com.nsn.quick4j.aop.annotation.AspectOrder;
import com.nsn.quick4j.aop.proxy.IAspect;
import com.nsn.quick4j.aop.tx.TransactionAspect;
import com.nsn.quick4j.core.ClassHelper;
import com.nsn.quick4j.core.ClassScanner;
import com.nsn.quick4j.core.error.InitializationError;
import com.nsn.quick4j.kit.CollectionKit;
import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * aop帮助类
 * 用于初始化aop框架，以及提供获取某类的代理类列表方法
 *
 * @author donghao
 * @since 1.0
 */
public class AopHelper {

    /**
     * 日志记录对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    /**
     * 类扫描器对象
     */
    private static final ClassScanner CLASS_SCANNER = InstanceFactory.getClassScanner();

    /**
     * 切面类与切面实例map
     * key:切面Class
     * value:切面实例
     */
    private static final Map<Class<?>, IAspect> ASPECT_MAP = new HashMap<Class<?>, IAspect>();
    /**
     * 目标类与切面实例列表map
     * key：目标类Class
     * value：切面实例列表
     */
    private static final Map<Class<?>, List<IAspect>> CLASS_LIST_MAP = new HashMap<Class<?>, List<IAspect>>();

    /**
     * 静态代码块初始化工作
     */
    static{
        init();
    }

    public static void init(){
        try{
            LOGGER.info("[INFO] AopHelper：初始化Aop容器开始");
            //初始化切面实例
            initAspectInstance();
            //创建徐代理对象与切面列表关系
            Set<Class<?>> aspectSet = ASPECT_MAP.keySet();
            for(Class<?> cls : aspectSet){
                List<Class<?>> classList = null;
                //判断是否是事务切面
                if(cls.equals(TransactionAspect.class)){
                    //事务切面，获取所有service类
                    classList = ClassHelper.getServiceClassList();
                }else{
                    //创建set去重
                    Set<Class<?>> classSet = new HashSet<Class<?>>();
                    //非事务切面，获取指定包名
                    String packagePath = cls.getAnnotation(Aspect.class).pkPath();
                    if(StringKit.isNotEmpty(packagePath)){
                        //获取指定包下所有类
                        List<Class<?>> pkClassList = CLASS_SCANNER.getClassList(packagePath);
                        CollectionKit.addListToSet(classSet,pkClassList);
                    }
                    //获取注解类别类
                    Class<? extends Annotation>[] annoArray = cls.getAnnotation(Aspect.class).annoType();
                    for(Class<? extends Annotation> annoClass : annoArray){
                        List<Class<?>> annoClassList = ClassHelper.getClassListByAnnotation(annoClass);
                        CollectionKit.addListToSet(classSet,annoClassList);
                    }
                    //获取数组指定的类
                    Class<?>[] clsArray = cls.getAnnotation(Aspect.class).clsArray();
                    CollectionKit.addArrayToSet(classSet,clsArray);
                    //转换为List
                    classList = CollectionKit.setToList(classSet);
                }
                //添加
                addAspectList(classList,cls);
            }
            //排序切面列表
            sortClassListMap();
            LOGGER.info("[INFO] AopHelper：初始化Aop容器结束");
        }catch(Exception e){
            //记录错误日志
            LOGGER.error("Initialization AopHelper error!",e);
            //抛出初始化错误
            throw new InitializationError("Initialization AopHelper error!",e);
        }
    }

    /**
     * 添加切面对象列表
     * @param classList
     */
    private static void addAspectList(List<Class<?>> classList,Class<?> aspectClass){
        for(Class<?> cls : classList){
            /**
             * 判断map中是否存在
             */
            List<IAspect> aspectList = null;
            if(!CLASS_LIST_MAP.containsKey(cls)){
                //创建List
                aspectList = new ArrayList<IAspect>();
                //加入容器
                CLASS_LIST_MAP.put(cls,aspectList);
            }else{
                aspectList = CLASS_LIST_MAP.get(cls);
            }
            //添加列表元素
            aspectList.add(ASPECT_MAP.get(aspectClass));
        }
    }

    /**
     * 将CLASS_LIST_MAP中的每个List中的Aspect按照OrderValue的值进行排序
     */
    public static void sortClassListMap(){
        for(Map.Entry<Class<?>,List<IAspect>> entry : CLASS_LIST_MAP.entrySet()){
            //获取列表
            List<IAspect> aspectList = entry.getValue();
            //排序列表
            CollectionKit.sortList(aspectList, new Comparator<IAspect>() {
                @Override
                public int compare(IAspect aspect1, IAspect aspect2) {
                    //若标注注解则排序
                    if(aspect1.getClass().getAnnotation(AspectOrder.class)!=null &&
                            aspect2.getClass().getAnnotation(AspectOrder.class)!=null ){
                        int aspect1Order = aspect1.getClass().getAnnotation(AspectOrder.class).value();
                        int aspect2Order = aspect2.getClass().getAnnotation(AspectOrder.class).value();
                        //升序
                        return aspect1Order - aspect2Order;
                    }
                    //否则二者相对位置不变
                    return 0;
                }
            });
        }
    }


    /**
     * 初始化切面实例
     * @return
     */
    public static void initAspectInstance() {
        //添加事务代理
        ASPECT_MAP.put(TransactionAspect.class,new TransactionAspect());
        //获取用户包下实现proxy接口的所有类
        List<Class<?>> ProxyClassList = ClassHelper.getClassListByAnnotation(Aspect.class);
        //创建切面实例并加入map
        for (Class<?> cls : ProxyClassList) {
            IAspect instance = (IAspect) ReflectKit.newInstance(cls);
            ASPECT_MAP.put(cls, instance);
        }
    }

    /**
     * 获取aspect实例对象
     * @param aspectClass
     * @return
     */
    public static IAspect getAspectInstance(Class<?> aspectClass){
        return ASPECT_MAP.get(aspectClass);
    }

    /**
     * 获取aop容器
     * @return
     */
    public static Map<Class<?>, List<IAspect>> getClassAspectListMap(){
        return CLASS_LIST_MAP;
    }

    /**
     * 通过Class获取对应代理切面列表
     * @param cls
     * @return 切面代理列表，若不存在，返回null
     */
    public static List<IAspect> getAspectProxyList(Class<?> cls){
        return CLASS_LIST_MAP.get(cls);
    }
}
