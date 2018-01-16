package com.nsn.quick4j.mvc.viewResolver.impl;

import com.nsn.quick4j.core.ConfigHelper;
import com.nsn.quick4j.kit.WebKit;
import com.nsn.quick4j.mvc.bean.Data;
import com.nsn.quick4j.mvc.bean.View;
import com.nsn.quick4j.mvc.viewResolver.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author donghao
 * @since 1.0
 */
public class DefaultViewResolver implements ViewResolver{
    /**
     * 视图解析
     *
     * @param request            请求对象
     * @param response           响应对象
     * @param actionMethodResult Action 方法返回值（View：jsp/Result:json数据）
     */
    @Override
    public void resolveView(HttpServletRequest request, HttpServletResponse response, Object actionMethodResult) {
        /**
         1、判断result类型
         2、分View和Data类型处理
         */
        Object result = actionMethodResult;
        if(result == null){
            return;
        }
        //result 非空时处理
        if(result instanceof View){
            //View类型处理（转发/重定向）
            /*
            1、判断是转发还是重定向
            2、重定向处理
            3、转发处理
             */
            View view = (View) result;
            //重定向处理
            if(view.isRedirect()){
                //获取路径
                String redirectPath = view.getPath();
                WebKit.redirectRequest(redirectPath,request,response);
                return;
            }
            //转发处理
            /*
            1、获取路径
            2、将Map数据封装如Response
            3、转发
             */
            String forwordPath = ConfigHelper.getAppJspPath() + view.getPath();
            //获取model
            Map<String,Object> model = view.getModel();
            for(Map.Entry<String,Object> entry : model.entrySet()){
                //获取key value
                String key = entry.getKey();
                Object value = entry.getValue();
                //设入request
                request.setAttribute(key,value);
            }
            WebKit.forwardRequest(forwordPath,request,response);

        }else if(result instanceof Data){
            //Data类型处理:json
            Data data = (Data) result;
            WebKit.writeJSON(response,data.getModel());
        }
    }
}
