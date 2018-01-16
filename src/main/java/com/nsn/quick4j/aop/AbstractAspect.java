package com.nsn.quick4j.aop;

import com.nsn.quick4j.aop.proxy.IAspect;
import com.nsn.quick4j.aop.proxy.AspectChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 *
 * 抽象切面代理
 * 实现Proxy接口，定义切面代理模板
 * 用于被继承以标识该类为切面代理类
 * 实现begin、before、after、error、end
 * @author donghao
 * @since 1.0
 */
public abstract class AbstractAspect implements IAspect {

    /**
     * 日志记录对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAspect.class);

    @Override
    public Object doAspect(AspectChain aspectChain) throws Throwable {
        //声明返回值
        Object result = null;
        //获取相关参数
        Class<?> targetClass = aspectChain.getTargetClass();
        Method targetMethod = aspectChain.getTargetMethod();
        Object[] methodParams = aspectChain.getMethodParams();
        Object targetObject = aspectChain.getTargetObject();
        //判断该方法是否执行
        if(intercept(targetClass,targetMethod,methodParams)){
            //判断该方法是否嵌入切面
            if(filter(targetClass,targetMethod,methodParams)){
                //开始方法
                begin();
                try{
                    //前置方法
                    before(targetClass,targetMethod,methodParams,targetObject);
                    //执行链式代理
                    result = aspectChain.doAspectChain();
                    //后置方法
                    after(targetClass,targetMethod,methodParams,targetObject);
                }catch (Exception e){
                    //日志记录
                    LOGGER.error("aop invoke error:"+targetClass.getName()+"@"+targetMethod.getName(),e);
                    //错误方法
                    error(targetClass,targetMethod,methodParams,targetObject,e);
                    //抛出异常
                    throw new RuntimeException("aop invoke error:"+targetClass.getName()+"@"+targetMethod.getName(),e);
                }finally {
                    //最终方法
                    end();
                }
            }else{
                //针对该方法该切面不进行切入，执行链式代理
                result = aspectChain.doAspectChain();
            }
        }else{
            //拦截目标方法后执行操作
            doIntercept(targetClass,targetMethod,methodParams);
        }
        return result;
    }

    /**
     * 定义开始方法供子类复写
     */
    public void begin(){}

    /**
     * 定义前置方法供子类复写
     */
    public void before(Class<?> targetClass, Method targetMethod, Object[] methodParams,Object targetObject){}

    /**
     * 定义后置方法供子类复写
     */
    public void after(Class<?> targetClass, Method targetMethod, Object[] methodParams,Object targetObject){}

    /**
     * 定义错误方法供子类复写
     */
    public void error(Class<?> targetClass, Method targetMethod, Object[] methodParams,Object targetObject,Exception e){}

    /**
     * 定义最终方法供子类复写
     */
    public void end(){}

    /**
     *  拦截指定方法执行，以及该切面后续切面的执行
     *
     * 定义拦截方法供子类复写
     * 用于实现方法级别的拦截
     * @param targetClass 目标类Class
     * @param targetMethod 目标类方法
     * @param methodParams 目标类方法参数
     * @return 当前方法是否代理
     */
    public boolean intercept(Class<?> targetClass, Method targetMethod, Object[] methodParams){
        return true;
    }


    /**
     *  拦截指定方法后执行的操作
     *
     * 定义拦截方法后操作供子类复写
     * 用于实现方法级别的拦截
     * @param targetClass 目标类Class
     * @param targetMethod 目标类方法
     * @param methodParams 目标类方法参数
     * @return 当前方法是否代理
     */
    public void doIntercept(Class<?> targetClass, Method targetMethod, Object[] methodParams){

    }
    /**
     * 过滤指定方法不插入切面
     *
     * 定义过滤方法供子类复写
     * 用于实现方法级别的拦截
     * @param targetClass 目标类Class
     * @param targetMethod 目标类方法
     * @param methodParams 目标类方法参数
     * @return 当前方法是否代理
     */
    public boolean filter(Class<?> targetClass, Method targetMethod, Object[] methodParams){return true;};


}
