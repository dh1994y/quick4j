package com.nsn.quick4j.aop.annotation;

import java.lang.annotation.*;

/**
 * Aspect注解：切面
 * 保留策略：运行时
 *
 * @author donghao
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 指定拦截的包名
     * @return
     */
    String pkPath() default "";

    /**
     * 代理的注解类类别
     * @return
     */
    Class<? extends Annotation>[] annoType() default {};

    /**
     * 代理的指定类数组中的类
     * @return
     */
    Class<?>[] clsArray() default {};
}
