package com.nsn.quick4j.orm;

/**
 * 主键生成策略枚举
 * @author donghao
 * @since 1.0
 */
public enum PKeyPolicy {

    AUTO("mysql_native"),//mysql底层自增长
    UUID("uuid"),//uuid唯一字符串
    USER("user_define");//用户自定义插入

    private String policy;//主键生成策略
    PKeyPolicy(String policy){
        this.policy = policy;
    }
}
