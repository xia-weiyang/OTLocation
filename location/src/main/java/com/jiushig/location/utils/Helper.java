package com.jiushig.location.utils;

/**
 * Created by zk on 2018/2/25.
 */

public class Helper {

    /**
     * 检查字符串是不是为null
     *
     * @param strings
     * @return
     */
    public static boolean isEmptyString(String... strings) {
        if (strings != null) {
            for (String str : strings) {
                if (str == null || str.isEmpty()) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
