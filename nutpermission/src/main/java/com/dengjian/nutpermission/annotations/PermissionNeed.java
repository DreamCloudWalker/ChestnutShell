package com.dengjian.nutpermission.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被此注解修饰的方法，会在方法执行之前去申请相应的权限，只有用户授予权限，被修饰的方法体才会执行
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionNeed {
    String[] permissions();         // 需要申请的权限,支持多个，需要传入String数组
    int requestCode() default 0;    // 此次申请权限之后的返回码
}
