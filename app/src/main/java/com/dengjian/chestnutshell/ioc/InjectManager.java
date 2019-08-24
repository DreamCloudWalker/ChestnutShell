package com.dengjian.chestnutshell.ioc;

import android.app.Activity;
import android.view.View;

import com.dengjian.chestnutshell.ioc.annotation.ContentView;
import com.dengjian.chestnutshell.ioc.annotation.EventBase;
import com.dengjian.chestnutshell.ioc.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {

    public static void inject(Activity activity) {
        injectLayout(activity);
        injectView(activity);
        injectEvent(activity);
    }

    private static void injectEvent(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                // 获取onClick注解上的注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (null != annotationType) {
                    // 获取EventBase注解
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (null != eventBase) {
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listenerType();
                        String callbackListener = eventBase.callbackListener();

                        try {
                            // 通过注解类型，获取OnClick注解的value值
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            // 执行value方法获取注解的值
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                            handler.addMethod(callbackListener, method);
                            Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                    new Class[]{listenerType}, handler);

                            for (int viewId : viewIds) {
                                View view = activity.findViewById(viewId);
                                // 获取指定方法
                                Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                setter.invoke(view, listener);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static void injectView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            // 获取所有属性上的注解
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (null != injectView) {
                int viewId = injectView.value();
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    Object view = method.invoke(activity, viewId);  // 执行findViewById，获得返回值
                    field.setAccessible(true);
                    field.set(activity, view);    // 给控件赋值
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectLayout(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (null != contentView) {
            int layoutId = contentView.value();

//            activity.setContentView(layoutId);

            try {
                // 获取指定方法setContentView
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
