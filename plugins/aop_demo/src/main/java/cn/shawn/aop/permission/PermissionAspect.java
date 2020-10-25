package cn.shawn.aop.permission;

import android.util.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspect {

    private static final String TAG = "PermissionAspect";

    @Pointcut("execution(* *.*(..)) && @annotation(RequestPermission)")
    public void RequestPermission() { }

    @Before("RequestPermission()")
    public void beforeRequestPermission(JoinPoint joinPoint) {
        Log.e(TAG, "class "+joinPoint.getSignature().getClass());
        Log.e(TAG, "beforeRequestPermission: "+joinPoint.getSignature().getName());
    }
}
