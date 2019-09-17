package com.dengjian.chestnutshell.ioc.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnTouchListener", listenerType = View.OnTouchListener.class, callbackListener = "onTouch")
public @interface OnTouch {
    int[] value();
}
