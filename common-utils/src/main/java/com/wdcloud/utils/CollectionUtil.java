package com.wdcloud.utils;

import java.util.*;

/**
 * Created by zijiao on 2/10/17.
 */
public class CollectionUtil {

    /**
     * 去掉重复项
     *
     * @param objList
     */
    public static List<Object> repeatList(List<String> objList) {
        List<Object> list = null;

        if (objList != null && objList.size() > 0) {
            Set<Object> set = new HashSet<>(objList);
            list = new ArrayList<>();
            list.clear();
            list.addAll(set);
        }

        return list;
    }

    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
    public static boolean isNotNullAndEmpty(Collection collection) {
        return !isNullOrEmpty(collection);
    }

    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }
    public static boolean isNotNullAndEmpty(Map map) {
        return !isNullOrEmpty(map);
    }

}
