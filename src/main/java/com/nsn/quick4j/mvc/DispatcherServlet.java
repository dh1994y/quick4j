package com.nsn.quick4j.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nsn.quick4j.InstanceFactory;
import com.nsn.quick4j.kit.WebKit;
import com.nsn.quick4j.mvc.handler.Handler;
import com.nsn.quick4j.mvc.handler.invoker.HandlerInvoker;
import com.nsn.quick4j.mvc.handler.mapping.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nsn.quick4j.core.ConfigHelper;
import com.nsn.quick4j.HelperLoader;

/**
 * 框架核心控制器
 * 用于请求的分发控制
 *
 * @author donghao
 * @since 1.0
 */
@WebServlet(name = "dispatcher", urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    /**
     * 序列号，序列化标识
     */
    private static final long serialVersionUID = 1L;
    /**
     * 日志记录属性
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    /**
     * 处理器映射器
     */
    private HandlerMapping handlerMapping = InstanceFactory.getHandlerMapping();
    /**
     * 处理器调度器
     */
    private HandlerInvoker handlerInvoker = InstanceFactory.getHandlerInvoker();

    /**
     * 重写init方法用于获取已注册的jspServlet和默认Servlet，修改其映射规则
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        LOGGER.info("[INFO] -------------------------------------------------------");
        LOGGER.info("[INFO] 初  始  化  开  始");
        LOGGER.info("[INFO] -------------------------------------------------------");
        //初始化相关helper类
        HelperLoader.init();
        //获取ServletContext
        ServletContext context = config.getServletContext();
        //注册处理Jsp的Servlet
        ServletRegistration jspServlet = context.getServletRegistration("jsp");
        //添加映射规则，后添加的映射规则覆盖前者的
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        //注册处理Asset的默认Servlet
        ServletRegistration defaultServlet = context.getServletRegistration("default");
        //添加映射规则，后添加的规则覆盖前者的
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

        LOGGER.info("[INFO] -------------------------------------------------------");
        LOGGER.info("[INFO] 初  始  化  完  成");
        LOGGER.info("[INFO] -------------------------------------------------------");
    }

    /**
     * 核心处理映射流程
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");
        //获取请求路径和请求方法
        String requestPath = request.getPathInfo();
        String requestMethod = request.getMethod().toLowerCase();
        //判断是否是项目根路径
        if("/".equals(requestPath)){
            //重定向到index.jsp
            WebKit.forwardRequest(ConfigHelper.getAppJspPath()+"index.jsp",request,response);
            return;
        }
        //获取处理器Handler
        Handler handler = handlerMapping.getHandler(requestMethod, requestPath);
        //判断处理器是否为空
        if (handler == null) {
            try{
                //添加错误日志
                LOGGER.error("this mapping path not exist:" + requestMethod + ":" + requestPath);
                //抛出运行时异常
                throw new RuntimeException("this mapping path not exist:" + requestMethod + ":" + requestPath);
            }catch(Exception e){
                // 跳转到 404 页面
                WebKit.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage(), response);
                return;
            }
        }
        //初始化数据上下文
        DataContext.init(request,response);
        //handler处理
        try{
            //调度执行处理器
            handlerInvoker.invokeHandler(request,response,handler);
        }catch (Exception e){
            //添加错误日志
            LOGGER.error("invoke the handler error:" + requestMethod + ":" + requestPath);
            //抛出运行时异常
            throw new RuntimeException("invoke the handler error:" + requestMethod + ":" + requestPath);
        }finally {
            //销毁数据上下文
            DataContext.destory();
        }
    }
}
