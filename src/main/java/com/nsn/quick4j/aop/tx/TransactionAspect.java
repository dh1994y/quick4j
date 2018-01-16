package com.nsn.quick4j.aop.tx;

import com.nsn.quick4j.aop.annotation.Aspect;
import com.nsn.quick4j.aop.annotation.AspectOrder;
import com.nsn.quick4j.aop.proxy.IAspect;
import com.nsn.quick4j.aop.proxy.AspectChain;
import com.nsn.quick4j.aop.tx.annotation.Service;
import com.nsn.quick4j.aop.tx.annotation.Transaction;
import com.nsn.quick4j.dao.DBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 *
 * 事务切面
 * 针对需要进行事务管理的方法提供一种事务管理模式：
 * 若当前方法已开启事务则不再开启新的事务
 * 若当前方法未开启事务则开启新的事务
 * @author donghao
 * @since 1.0
 */
@AspectOrder(-1)
@Aspect(annoType = {Service.class})
public class TransactionAspect implements IAspect {

    /**
     * 日志记录对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionAspect.class);

    /**
     * 方法是否开启事务状态容器，绑定当前执行线程，初始化为false
     */
    ThreadLocal<Boolean> statusContainer = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    } ;

    /**
     * 执行链式代理
     *
     * @param aspectChain 代理链对象
     * @return 代理链上代理方法的返回值
     * @throws Throwable
     */
    @Override
    public Object doAspect(AspectChain aspectChain) throws Throwable {

        Object result = null;
        //获取相关参数
        Method method = aspectChain.getTargetMethod();
        //判断是否是事务方法，且是否已开启事务
        boolean status = statusContainer.get();
        if(!status && method.isAnnotationPresent(Transaction.class)){
            try{
                //设置该线程绑定方法已经开启事务
                statusContainer.set(true);
                //开启事务
                DBHelper.beginTransaction();
                LOGGER.info("[Quick4j-Transaction-Manage] begin transaction");
                //执行代理
                result = aspectChain.doAspectChain();
                //提交事务
                DBHelper.commitTransaction();
                LOGGER.info("[Quick4j-Transaction-Manage] commit transaction");
            }catch (Exception e){
                //事务回滚
                DBHelper.rollbackTransaction();
                LOGGER.info("[Quick4j-Transaction-Manage] rollback transaction",e);
            }finally {
                //移除状态容器标识
                statusContainer.remove();
            }
        }else{
            //执行目标方法
            result = aspectChain.doAspectChain();
        }
        return result;
    }
}
