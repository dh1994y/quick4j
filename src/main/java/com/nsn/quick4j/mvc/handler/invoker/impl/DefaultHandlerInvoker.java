package com.nsn.quick4j.mvc.handler.invoker.impl;

import com.nsn.quick4j.InstanceFactory;
import com.nsn.quick4j.aop.AopHelper;
import com.nsn.quick4j.aop.proxy.IAspect;
import com.nsn.quick4j.aop.proxy.ProxyManager;
import com.nsn.quick4j.ioc.BeanHelper;
import com.nsn.quick4j.ioc.IocHelper;
import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.mvc.formInjection.FormInjection;
import com.nsn.quick4j.mvc.handler.Handler;
import com.nsn.quick4j.mvc.handler.invoker.HandlerInvoker;
import com.nsn.quick4j.mvc.viewResolver.ViewResolver;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author donghao
 * @since 1.0
 */
public class DefaultHandlerInvoker implements HandlerInvoker {

    /**
     * 获取表单数据注入器
     */
    private FormInjection defaultFormInjection = InstanceFactory.getDefaultFormInjection();
    private FormInjection fileUploadFormInjection = InstanceFactory.getFileUploadFormInjection();
    /**
     * 视图解析器
     */
    private ViewResolver viewResolver = InstanceFactory.getViewResolver();

    /**
     * 调度执行处理器
     *
     * @param request  请求对象
     * @param response 相应对象
     * @param handler  处理器
     * @throws Exception 异常
     */
    @Override
    public void invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception {
        /**
         1、获取ControllerClass
         2、获取Method
         3、创建Controller实例
         4、进行表单数据注入
         5、反射执行Method
         6、试图解析
         */
        Class<?> controllerClass = handler.getControllerClass();
        Method actionMethod = handler.getActionMethod();
        //创建Controller实例，判断是否是代理对象
        Object instance = null;
        List<IAspect> aspectList = AopHelper.getAspectProxyList(controllerClass);
        if (aspectList != null) {
            //创建代理对象
            instance = ProxyManager.createProxy(controllerClass, aspectList);
        } else {
            //创建Bean实例对象
            instance = ReflectKit.newInstance(controllerClass);
        }
        //Controller依赖关系注入
        IocHelper.injectBean(BeanHelper.getBeanMap(), controllerClass, instance);
        //表单数据注入,判断是否是文件上传表单
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        FormInjection formInjection = null;
        if (!isMultipart) {
            formInjection = defaultFormInjection;
            //判断是否存在模型驱动
            Object model = formInjection.getModel(controllerClass, instance);
            if (model != null) {
                //注入模型
                formInjection.formInject(model.getClass(), model, request);
            }
            formInjection.formInject(controllerClass,instance,request);
        } else {
            //文件上传处理
            formInjection = fileUploadFormInjection;
            formInjection.formInject(controllerClass,instance,request);
        }

        Object result = ReflectKit.invokeMethod(instance, actionMethod, null);
        //试图解析
        viewResolver.resolveView(request, response, result);
    }
}
