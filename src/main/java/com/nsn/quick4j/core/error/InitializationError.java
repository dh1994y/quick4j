package com.nsn.quick4j.core.error;

/**
 *
 * 自定义错误：初始化错误
 *
 * @author donghao
 * @since 1.0
 */
public class InitializationError extends Error{

    public InitializationError(){
        super();
    }

    public InitializationError(String message){
        super(message);
    }

    public InitializationError(String message,Throwable cause){
        super(message,cause);
    }

    public InitializationError(Throwable cause){
        super(cause);
    }
}
