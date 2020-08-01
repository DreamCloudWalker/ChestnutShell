package com.dengjian.nutpermission.aspect;

import android.content.Context;
import android.util.Log;

import com.dengjian.nutpermission.annotations.PermissionDenied;
import com.dengjian.nutpermission.annotations.PermissionDeniedForever;
import com.dengjian.nutpermission.annotations.PermissionNeed;
import com.dengjian.nutpermission.interfaces.IPermissionCallback;
import com.dengjian.nutpermission.utils.ApplicationUtil;
import com.dengjian.nutpermission.utils.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspect {
    private static final String TAG = "PermissionAspect";
    private final String pointcutExpression =
            "execution(@com.dengjian.nutpermission.annotations.PermissionNeed * *(..)) && @annotation(permissionNeed)";

    @Pointcut(value = pointcutExpression, argNames = "permissionNeed")
    public void requestPermission(PermissionNeed permissionNeed) {
        Log.d(TAG,"pointCut 定义切入点");
    }

    @Around("requestPermission(permissionNeed)")
    public void doPermission(final ProceedingJoinPoint joinPoint, PermissionNeed permissionNeed) {
        PermissionAspectActivity.startActivity(getContext(joinPoint), permissionNeed.permissions(),
                permissionNeed.requestCode(), new IPermissionCallback() {
            @Override
            public void granted(int requestCode) {
                // 如果授予，那么执行joinPoint原方法体
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void denied(int requestCode) {
                PermissionUtil.invokeAnnotation(joinPoint.getThis(), PermissionDenied.class, requestCode);
            }

            @Override
            public void deniedForever(int requestCode) {
                PermissionUtil.invokeAnnotation(joinPoint.getThis(), PermissionDeniedForever.class, requestCode);
            }
        });
    }

    private Context getContext(final ProceedingJoinPoint joinPoint) {
        final Object obj = joinPoint.getThis();
        if (obj instanceof Context) {// 如果切入点是一个类？那么这个类的对象是不是context？
            return (Context) obj;
        } else {    // 如果切入点不是Context的子类呢？ //jointPoint.getThis，其实是得到切入点所在类的对象
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {//
                if (args[0] instanceof Context) {// 看看第一个参数是不是context
                    return (Context) args[0];
                } else {
                    return ApplicationUtil.getApplication();// 如果不是，那么就只好hook反射了
                }
            } else {
                return ApplicationUtil.getApplication();// 如果不是，那么就只好hook反射了
            }
        }
    }
}
