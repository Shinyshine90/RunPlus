package cn.shawn.base.utils;

import java.util.UUID;

public class UuidUtil {

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
