package cn.shawn.aop.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//这里必须使用Class、Runtime 确保dex打包阶段注解还在，这样gradle插件才能正常工作
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface RequestPermission {
}
