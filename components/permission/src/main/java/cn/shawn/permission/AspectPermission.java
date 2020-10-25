package cn.shawn.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cn.shawn.permission.annotation.RequestPermission;

@Aspect
public class AspectPermission {

    public static final String TAG = "AspectPermission";

    private static final int REQUEST_CODE = 0x1024;

    @Pointcut("execution(* *.*(..))")
    public void methodPointCut() {}

    @Pointcut("@annotation(requestPermission)")
    public void requestAnnotation(RequestPermission requestPermission) {}

    @Around(value = "methodPointCut() && requestAnnotation(requestPermission)")
    public Object requestPermissionInternal(ProceedingJoinPoint joinPoint, RequestPermission requestPermission) throws Throwable {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        if (requestPermission == null) {
            Log.e(TAG, "requestPermissionInternal: null requestPermission");
        } else {
            Object target =joinPoint.getTarget();
            String[] permissions = requestPermission.permissions();
            List<String> notPermitPermissions = checkPermissions(target, permissions);
            if (notPermitPermissions.isEmpty()) {
                Log.e(TAG, "requestPermissionInternal: all permitted");
            } else {
                requestPermissions(target, notPermitPermissions);
            }
            Log.e(TAG, "requestPermissionInternal: " + Arrays.toString(requestPermission.permissions()));
        }
        Object result = joinPoint.proceed(joinPoint.getArgs());
        return result;
    }

    private List<String> checkPermissions(Object target, String[] permissions) {
        Activity activity = null;
        if (target instanceof Activity) {
            activity = (Activity) target;
        } else if (target instanceof Fragment) {
            activity = ((Fragment) target).getActivity();
        } else if (target instanceof android.app.Fragment) {
            activity = ((android.app.Fragment) target).getActivity();
        }
        if (activity == null) {
            return Arrays.asList(permissions);
        }
        List<String> notPermitPermissions = new ArrayList<>();
        for (String permission : permissions) {
            boolean permit = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, permission);
            if (!permit) {
                notPermitPermissions.add(permission);
            }
        }
        return notPermitPermissions;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions(Object target, List<String> permissions) {
        String[] permissionArr = permissions.toArray(new String[permissions.size()]);
        if (target instanceof Activity) {
            ((Activity) target).requestPermissions(permissionArr, REQUEST_CODE);
        } else if (target instanceof Fragment) {
            ((Fragment) target).requestPermissions(permissionArr, REQUEST_CODE);
        } else if (target instanceof android.app.Fragment) {
            ((android.app.Fragment) target).requestPermissions(permissionArr, REQUEST_CODE);
        }
    }

    public static void onActivityResult(Object target, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != REQUEST_CODE) return;

    }

}
