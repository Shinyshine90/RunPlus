package cn.shawn.aop;

import android.util.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ActivityAspect {

    /**
     * 作用在android.app.Activity及其子类上
     * 匹配以onXXX命名 任意入参 任意返回值的方法
     * Note:: #Around
     */
    @Around("execution(* android.app.Activity.on**(..))")
    public Object printActivityLifeCycle(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("AspectTest", "before "+joinPoint.getSignature());
        Object result = joinPoint.proceed(joinPoint.getArgs());
        Log.e("AspectTest", "after "+joinPoint.getSignature());
        return "HelloWorld".equals(result) ? "HelloWorldInject" : result;
    }

    @Around("execution(void android.app.Activity.on**(..))")
    public void onResumeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i("helloAOP", "aspect:::" + joinPoint.getSignature());
        joinPoint.proceed(joinPoint.getArgs());
    }

}
