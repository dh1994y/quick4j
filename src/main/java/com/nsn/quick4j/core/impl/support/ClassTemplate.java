package com.nsn.quick4j.core.impl.support;

import com.nsn.quick4j.kit.ClassKit;
import com.nsn.quick4j.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * 用于获取类的模板类
 *
 * @author donghao
 * @since 1.0
 */
public abstract class ClassTemplate {

    /**
     * 日志记录属性
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassTemplate.class);

    protected final String packageName;

    protected ClassTemplate(String packageName){
        this.packageName = packageName;
    }

    /**
     *
     * @return
     */
    public List<Class<?>> getClassList() {
        // 声明存储结构
        List<Class<?>> classList = new ArrayList<Class<?>>();
        try {
            // 获取包名下资源
            Enumeration<URL> urls = ClassKit.getClassLoader().getResources(packageName.replace(".", "/"));
            // 遍历资源
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    // 获取资源协议
                    String protocol = url.getProtocol();
                    // 判断协议
                    if ("file".equals(protocol)) {
                        // class文件处理
                        String packagePath = url.getPath();
                        // 加载类并添加类Class对象到List
                        addClass(classList, packagePath, packageName);
                    }else if("jar".equals(protocol)){
                        //jar包资源文件处理
                        JarURLConnection jarConn = (JarURLConnection)url.openConnection();
                        //获取JarFile对象
                        JarFile jarFile = jarConn.getJarFile();
                        //获取枚举
                        Enumeration<JarEntry> jarEntrys = jarFile.entries();
                        while(jarEntrys.hasMoreElements()){
                            //获取当前枚举值
                            JarEntry jarEntry = jarEntrys.nextElement();
                            //获取当前枚举名
                            String jarEntryName = jarEntry.getName();
                            //判断是否是class文件
                            if(jarEntryName.endsWith(".class")){
                                //获取类全名
                                String className = jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replace("/",".");
                                //加载类
                                doAddClass(classList,className);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 日志记录
            LOGGER.error("get class List failure", e);
            // 抛出运行时异常
            throw new RuntimeException("get class List failure!", e);
        }
        return classList;
    }

    /**
     * 加载类并添加类Class对象到List
     *
     * @param classList    Class容器
     * @param packagePath 包路径
     * @param packageName 包名
     */
    private void addClass(List<Class<?>> classList, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            // 文件类型过滤：只列举class文件或文件夹
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        //遍历文件
        for (File file : files) {
            //获取文件名，不包含路径
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringKit.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                //加载类并添加到容器
                doAddClass(classList, className);
            } else {
                //子包路径
                String subPackagePath = fileName;
                if (StringKit.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                //子包名
                String subPackageName = fileName;
                if (StringKit.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classList, subPackagePath, subPackageName);
            }
        }
    }

    /**
     * 添加时执行的操作
     *
     * @param classList  Class容器
     * @param className 要添加类的限定名
     */
    private void doAddClass(List<Class<?>> classList, String className) {
        // 加载指定类，默认不执行static code block
        Class<?> cls = ClassKit.loadClass(className, false);
        // 添加指定类 类对象 到 List容器
        if(checkAddClass(cls)){
            classList.add(cls);
        }
    }

    /**
     * 验证是否允许添加类
     * @param cls 添加的类
     * @return
     */
    public abstract boolean checkAddClass(Class<?> cls);
}
