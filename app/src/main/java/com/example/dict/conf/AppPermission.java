package com.example.dict.conf;

import java.util.HashMap;
import java.util.Map;

public final class AppPermission {
    private AppPermission() {}
    private static final class SingleInstance { private static final AppPermission ins = new AppPermission(); }
    public static synchronized AppPermission getPermission() {
        return SingleInstance.ins;
    }

    private static final int TAG_READ_EXTERNAL_STORAGE = 0; // 读取存储权限

    private Map map = new HashMap();

//    public boolean checkPermission
}
