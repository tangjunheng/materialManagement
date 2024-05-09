package com.material.annotation;

import com.material.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 */
@Target(ElementType.METHOD) // 只能用于标注方法
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public @interface AutoFill {
    // 枚举数据库类型：UPDATE INSERT 用于标示自动填充的类型
    OperationType value();
}
