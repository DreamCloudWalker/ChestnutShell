package com.dengjian.chestnutshell.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)   // 作用在类上
@Retention(RetentionPolicy.RUNTIME) // jvm运行时通过反射获取注解的值
public @interface ContentView {
    int value();
}
