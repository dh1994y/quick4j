package com.nsn.quick4j.mvc.formInjection.impl;

import com.nsn.quick4j.ioc.annotation.Inject;
import com.nsn.quick4j.kit.ReflectKit;
import com.nsn.quick4j.mvc.converter.Converter;
import com.nsn.quick4j.mvc.converter.ConverterHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传表单注入处理
 *
 * @author donghao
 * @since 1.0
 */
public class FileUploadFormInjection extends AbstractFormInject {

    /**
     * 日志记录对象
     */
    private static Logger LOGGER = LoggerFactory.getLogger(FileUploadFormInjection.class);

    @Override
    public void formInject(Class<?> actionClass, Object instance, HttpServletRequest request) {
        //创建工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //工厂设置
        //文件在内存的最大大小设置  单位字节B
        Field maxMemorySizeField = ReflectKit.getFieldNoException(actionClass, "maxMemorySize");
        if (maxMemorySizeField != null) {
            int MaxMemorySize = (Integer) ReflectKit.getFieldValue(instance, maxMemorySizeField);
            factory.setSizeThreshold(MaxMemorySize);
        } else {
            //默认超过1m先存到临时目录
            factory.setSizeThreshold(1024 * 1024);
        }
        //文件临时存储位置设置
        Field fileDirectoryField = ReflectKit.getFieldNoException(actionClass, "tempDirectory");
        //默认临时目录
        String fileDirectory = "D:/tempDirectory";
        if (fileDirectoryField != null) {
            fileDirectory = (String) ReflectKit.getFieldValue(instance, maxMemorySizeField);
        }
        File repository = new File(fileDirectory);
        if (!repository.exists()) {
            repository.mkdirs();
        }
        factory.setRepository(repository);
        //创建文件上传处理器
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        try {

            //解析请求
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = iter.next();
                if (item.isFormField()) {
                    //非文件处理
                    formFieldProcess(actionClass, instance, item);
                } else {
                    //文件处理
                    fileFieldProcess(actionClass, instance, item);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("execute fileUpload error!:" + e);
            throw new RuntimeException("execute fileUpload error!:" + e);
        }
    }

    /**
     * 文件表单数据处理
     *
     * @param actionClass
     * @param instance
     * @param item
     * @throws Exception
     */
    public void fileFieldProcess(Class<?> actionClass, Object instance, FileItem item) throws Exception {
        String fieldName = item.getFieldName();
        String fileName = item.getName();
        long fileSize = item.getSize();
        if (fileName == null || fileName.length() == 0) {
            return;
        }
        //设置文件名
        Field fileNameField = ReflectKit.getFieldNoException(actionClass, fieldName + "FileName");
        if (fileNameField != null) {
            ReflectKit.setFieldValue(instance, fileNameField, fileName);
        }
        //设置文件大小
        Field fileSizeField = ReflectKit.getFieldNoException(actionClass, fieldName + "FileSize");
        if (fileSizeField != null) {
            ReflectKit.setFieldValue(instance, fileSizeField, fileSize);
        }
        //获取存储位置
        Field fileStoreDirField = ReflectKit.getFieldNoException(actionClass, fieldName + "FileStoreDir");
        String fileStoreDir = null;
        if (fileStoreDirField != null) {
            fileStoreDir = (String) ReflectKit.getFieldValue(instance, fileStoreDirField);
        } else {
            throw new RuntimeException("you should specify field named xxxFileStoreDir!");
        }
        //保存文件
        File fileDir = new File(fileStoreDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        Field fileField = ReflectKit.getFieldNoException(actionClass, fieldName + "File");
        if (fileField != null) {
            //在上传文件名前拼接uuid，保证同名文件不会重复
            File file = new File(fileDir, UUID.randomUUID().toString() + "-" + fileName);
            item.write(file);
            //注入到文件对象
            ReflectKit.setFieldValue(instance, fileField, file);
        }
        //删除临时文件
        item.delete();
    }

    /**
     * 非文件字段处理
     *
     * @param actionClass
     * @param instance
     * @param item
     */
    public void formFieldProcess(Class<?> actionClass, Object instance, FileItem item) {
        String name = item.getFieldName();
        String value = "";
        try {
            value = item.getString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("not support this encoding" + e);
        }
        //将name按“.”切割  注意转义
        String[] nameArray = name.split("\\.");
        //仅考虑 age  user.name 两种形式
        if (nameArray.length == 1) {
            //一级字段注入 eg:age
            String fieldName = nameArray[0];
            //注入字段
            injectField(actionClass, instance, fieldName, value, false);
        } else if (nameArray.length == 2) {
            //二级字段注入 eg:user.name
            String entityName = nameArray[0];
            String fieldName = name;
            //获取实体字段
            Field entityField = null;
            try {
                entityField = actionClass.getDeclaredField(entityName);
            } catch (NoSuchFieldException e) {
                //不做处理
            }
            if (entityField == null) {
                return;
            }
            //获取实体对象
            Object entityObj = ReflectKit.getFieldValue(instance, entityField);
            //判断是否实现模型驱动
            Object model = getModel(actionClass, instance);
            if (model == null || entityObj != model) {
                //若模型驱动为空或该字段非模型驱动对象时注入字段
                injectField(entityField.getType(), entityObj, fieldName, value, true);
            }
        }
    }

    /**
     * 注入字段数据
     *
     * @param beanClass      字段所属于的类的Class对象
     * @param instance       字段所属于的类的实例
     * @param fieldName      字段名
     * @param paramaValue    参数值
     * @param isSecondInject 该字段是否为实体类型
     */
    private void injectField(Class<?> beanClass, Object instance, String fieldName, String paramaValue, boolean isSecondInject) {
        Field field = null;
        //获取请求参数名
        String paramaName = fieldName;
        //若为二级注入，获取二级字段名
        if (isSecondInject) {
            fieldName = fieldName.split("\\.")[1];
        }
        //若前台名为数组名，将数组[]截掉
        if (fieldName.endsWith("[]")) {
            fieldName = fieldName.substring(0, fieldName.length() - 2);
        }
        //模型驱动处理
        //判断是否存在模型驱动
        if(!isSecondInject){
            //模型驱动只进行一级注入
            Object model = getModel(beanClass, instance);
            if (model != null) {
                //判断是否存在对应名字段
                Field modelField = ReflectKit.getFieldNoException(model.getClass(),fieldName);
                if(modelField!=null){
                    //注入
                    ReflectKit.setFieldValue(model,modelField,paramaValue);
                }
            }
        }
        //非模型驱动处理
        try {
            //获取同名字段
            field = beanClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            //无该字段时，不做处理
        }
        //存在该字段时且该字段未标注@Inject注解，进行注入
        if (field != null && !field.isAnnotationPresent(Inject.class)) {
            //获取字段类型
            Class<?> fieldType = field.getType();
            //获取指定类型转换器
            Converter converter = ConverterHelper.getConverter(fieldType);
            if (converter == null) {
                //未找到指定类型转换器，则日志记录并抛出异常
                LOGGER.debug("not have matcher Type Converter,targetClass Type:" + fieldType);
                throw new RuntimeException("not have matcher Type Converter,targetClass Type:" + fieldType);
            }
            //通过类型转换器获取转换值
            Object value = converter.convert(paramaValue);
            //通过反射注入
            ReflectKit.setFieldValue(instance, field, value);
        }
    }

}
