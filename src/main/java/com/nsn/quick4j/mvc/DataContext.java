package com.nsn.quick4j.mvc;

import com.nsn.quick4j.kit.ArrayKit;
import com.nsn.quick4j.kit.CastKit;
import com.nsn.quick4j.kit.CodecKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 数据上下文，针对每次请求即每个线程提供独有的数据上下文
 * 封装Servlet api，与Servlet Api进行解耦
 *
 * @author donghao
 * @since 1.0
 */
public class DataContext {

    /**
     * 使每个现成拥有各自的DataContext实例，即每次请求对应一个DataContext实例
     */
    private static final ThreadLocal<DataContext> DATA_CONTEXT_CONTAINER = new ThreadLocal<DataContext>();

    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * 初始化当前线程DataContext
     * @param request 请求对象
     * @param response 响应对应
     */
    public static void init(HttpServletRequest request,HttpServletResponse response){
        //创建DataContext实例并初始化
        DataContext dataContext = new DataContext();
        dataContext.request = request;
        dataContext.response = response;
        DATA_CONTEXT_CONTAINER.set(dataContext);
    }

    /**
     * 销毁当前线程对应的DataContext
     */
    public static void destory(){
        DATA_CONTEXT_CONTAINER.remove();
    }

    /**
     * 获取当前线程对应的DataContext
     * @return
     */
    public static DataContext getDataContext(){
        return DATA_CONTEXT_CONTAINER.get();
    }

    /**
     * 获取request对象
     * @return 请求对象
     */
    public static HttpServletRequest getRequest(){
        return getDataContext().request;
    }

    /**
     * 获取response对象
     * @return 响应对象
     */
    public static HttpServletResponse getResponse(){
        return getDataContext().response;
    }

    /**
     * 获取cookie数组
     * @return cookie数组
     */
    public static javax.servlet.http.Cookie[] getCookies(){
        return getRequest().getCookies();
    }

    /**
     * 获取session对象
     * @return session
     */
    public static HttpSession getSession(){
        return getRequest().getSession();
    }

    /**
     * 获取servletContext
     * @return servlet上下文
     */
    public static javax.servlet.ServletContext getServletContext(){
        return getRequest().getServletContext();
    }

    /**
     * 封装 Request 相关操作
     */
    public static class Request {

        /**
         * 将数据放入 Request 中
         */
        public static void put(String key, Object value) {
            getRequest().setAttribute(key, value);
        }

        /**
         * 从 Request 中获取数据
         */
        @SuppressWarnings("unchecked")
        public static <T> T get(String key) {
            return (T) getRequest().getAttribute(key);
        }

        /**
         * 移除 Request 中的数据
         */
        public static void remove(String key) {
            getRequest().removeAttribute(key);
        }

        /**
         * 从 Request 中获取所有数据
         */
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<String, Object>();
            Enumeration<String> names = getRequest().getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, getRequest().getAttribute(name));
            }
            return map;
        }
    }

    /**
     * 封装 Response 相关操作
     */
    public static class Response {

        /**
         * 将数据放入 Response 中
         */
        public static void put(String key, Object value) {
            getResponse().setHeader(key, CastKit.castString(value));
        }

        /**
         * 从 Response 中获取数据
         */
        @SuppressWarnings("unchecked")
        public static <T> T get(String key) {
            return (T) getResponse().getHeader(key);
        }

        /**
         * 从 Response 中获取所有数据
         */
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String name : getResponse().getHeaderNames()) {
                map.put(name, getResponse().getHeader(name));
            }
            return map;
        }
    }

    /**
     * 封装 Session 相关操作
     */
    public static class Session {

        /**
         * 将数据放入 Session 中
         */
        public static void put(String key, Object value) {
            getSession().setAttribute(key, value);
        }

        /**
         * 从 Session 中获取数据
         */
        @SuppressWarnings("unchecked")
        public static <T> T get(String key) {
            return (T) getSession().getAttribute(key);
        }

        /**
         * 移除 Session 中的数据
         */
        public static void remove(String key) {
            getSession().removeAttribute(key);
        }

        /**
         * 从 Session 中获取所有数据
         */
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<String, Object>();
            Enumeration<String> names = getSession().getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, getSession().getAttribute(name));
            }
            return map;
        }

        /**
         * 移除 Session 中所有的数据
         */
        public static void removeAll() {
            getSession().invalidate();
        }
    }

    /**
     * 封装 Cookie 相关操作
     */
    public static class Cookie {

        /**
         * 将数据放入 Cookie 中
         */
        public static void put(String key, Object value) {
            String strValue = CodecKit.encodeURL(CastKit.castString(value));
            javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(key, strValue);
            getResponse().addCookie(cookie);
        }

        /**
         * 从 Cookie 中获取数据
         */
        @SuppressWarnings("unchecked")
        public static <T> T get(String key) {
            T value = null;
            javax.servlet.http.Cookie[] cookieArray = getRequest().getCookies();
            if (ArrayKit.isNotEmpty(cookieArray)) {
                for (javax.servlet.http.Cookie cookie : cookieArray) {
                    if (key.equals(cookie.getName())) {
                        value = (T) CodecKit.decodeURL(cookie.getValue());
                        break;
                    }
                }
            }
            return value;
        }

        /**
         * 从 Cookie 中获取所有数据
         */
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<String, Object>();
            javax.servlet.http.Cookie[] cookieArray = getRequest().getCookies();
            if (ArrayKit.isNotEmpty(cookieArray)) {
                for (javax.servlet.http.Cookie cookie : cookieArray) {
                    map.put(cookie.getName(), cookie.getValue());
                }
            }
            return map;
        }
    }

    /**
     * 封装 ServletContext 相关操作
     */
    public static class ServletContext {

        /**
         * 将数据放入 ServletContext 中
         */
        public static void put(String key, Object value) {
            getServletContext().setAttribute(key, value);
        }

        /**
         * 从 ServletContext 中获取数据
         */
        @SuppressWarnings("unchecked")
        public static <T> T get(String key) {
            return (T) getServletContext().getAttribute(key);
        }

        /**
         * 移除 ServletContext 中的数据
         */
        public static void remove(String key) {
            getServletContext().removeAttribute(key);
        }

        /**
         * 从 ServletContext 中获取所有数据
         */
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<String, Object>();
            Enumeration<String> names = getServletContext().getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, getServletContext().getAttribute(name));
            }
            return map;
        }
    }
}
