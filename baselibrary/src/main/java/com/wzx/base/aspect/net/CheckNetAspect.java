package com.wzx.base.aspect.net;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.wzx.base.utils.NetUtils;
import com.wzx.base.utils.ToastUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class CheckNetAspect {
    @Pointcut("execution(@com.wzx.base.aspect.net.CheckNet * *(..))")
    public void checkNetBehavior() {

    }

    /**
     * 处理切面
     */
    @Around("checkNetBehavior()")
    public Object checkNet(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        //1.获取CheckNet注解
        MethodSignature methodSignature = (MethodSignature) sig;
        // 类名
        // String className = methodSignature.getDeclaringType().getSimpleName();
        // 方法名
        // String methodName = methodSignature.getName();
        CheckNet checkNet = methodSignature.getMethod().getAnnotation(CheckNet.class);
        //2.注解存在的情况下判断是否有网络
        if (checkNet != null) {
            //获取当前方法所在的类，这个操作主要是用于View类事件，所以可能是Activity、Fragment和View
            Object object = joinPoint.getThis();
            Context context = null;
            if (object instanceof Activity) {
                context = (Activity) object;
            } else if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                context = fragment.getActivity();
            } else if (object instanceof android.support.v4.app.Fragment) {
                android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) object;
                context = fragment.getActivity();
            } else if (object instanceof View) {
                View view = (View) object;
                context = view.getContext();
            }
            if (context != null) {
                if (!NetUtils.isNetworkAvalible(context)) {
                    ToastUtils.showToast(context, "请检查网络是否连接");
                    return null;
                }
            }
        }
        return joinPoint.proceed();
    }
}
