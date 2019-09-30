package com.dengjian.chestnutshell.ioc.annotation;

import com.dengjian.chestnutshell.ioc.typs.NetMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NetSubscribe {
    NetMode mode() default NetMode.AUTO;
}
