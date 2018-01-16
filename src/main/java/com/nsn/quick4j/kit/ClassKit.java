package com.nsn.quick4j.kit;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具类 用于加载指定包下的所有类
 *
 * @author donghao
 * @since 1.0
 */
public class ClassKit {
    /**
     * 日志记录属性
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassKit.class);

    /**
     * 获取当前线程的类加载器
     *
     * @return 当前线程所处上下文的类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取当前项目类根路径
     *
     * @return 类根路径
     */
    public static String getClassPath() {
        String classPath = "";
        URL resource = getClassLoader().getResource("");
        if (resource != null) {
            classPath = resource.getPath();
        }
        return classPath;
    }

    /**
     * 加载指定类
     *
     * @param className     被加载类的限定名
     * @param isInitialized 是否初始化标志
     * @return 被加载类的Class对象
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {

        try {
            // isInitialized为false时，类加载性能提升，不执行加载类的static code block
            return Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            // 日志记录
            LOGGER.error("load class failure", e);
            // 抛出运行时异常
            throw new RuntimeException("load class failure!", e);
        }
    }

    /**
     * 加载类 默认初始化
     *
     * @param className 加载的全类名
     * @return 被加载类的Class对象
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    /**
     * 是否为byte类型（包括包装类）
     *
     * @param type
     * @return
     */
    public static boolean isByte(Class<?> type) {
        return type.equals(byte.class) || type.equals(Byte.class);
    }

    /**
     * 是否为char类型（包括包装类）
     *
     * @param type
     * @return
     */
    public static boolean isChar(Class<?> type) {
        return type.equals(char.class) || type.equals(Character.class);
    }

    /**
     * 是否为short类型（包括包装类）
     *
     * @param type
     * @return
     */
    public static boolean isShort(Class<?> type) {
        return type.equals(short.class) || type.equals(Short.class);
    }

    /**
     * 是否为int类型（包括包装类）
     *
     * @param type
     * @return
     */
    public static boolean isInt(Class<?> type) {
        return type.equals(int.class) || type.equals(Integer.class);
    }

    /**
     * 是否为long类型（包括包装类）
     *
     * @param type
     * @return
     */
    public static boolean isLong(Class<?> type) {
        return type.equals(long.class) || type.equals(Long.class);
    }

    /**
     * 是否为float类型（包括包装类）
     *
     * @param type
     * @return
     */
    public static boolean isFloat(Class<?> type) {
        return type.equals(float.class) || type.equals(Float.class);
    }

    /**
     * 是否为boolean类型（包括包装类）
     *
     * @param type
     * @return
     */
    public static boolean isDouble(Class<?> type) {
        return type.equals(double.class) || type.equals(Double.class);
    }

    /**
     * 是否为boolean类型（包括包装类）
     *
     * @param type
     * @return
     */
    public static boolean isBoolean(Class<?> type) {
        return type.equals(boolean.class) || type.equals(Boolean.class);
    }

    /**
     * 是否为基本数据类型（包括包装类）
     * @param type
     * @return
     */
    public static boolean isBasicType(Class<?> type) {
        return isBoolean(type) || isByte(type) || isChar(type)
                || isShort(type) || isInt(type) || isLong(type)
                || isFloat(type) || isDouble(type);
    }

    /**
     * 是否为String类型
     *
     * @param type
     * @return
     */
    public static boolean isString(Class<?> type) {
        return type.equals(String.class);
    }

}
