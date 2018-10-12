package com.wdcloud.utils;

import java.util.*;

/**
 * List 工具类
 *
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/7/1.
 */
public class ListUtils {

    public static <T> boolean isNotEmpty(List<T> tList) {
        return !isEmpty(tList);
    }

    public static <T> boolean isEmpty(List<T> tList) {
        return tList == null || tList.isEmpty();
    }

    public static List<String> integerToString(List<Integer> list) {
        List<String> stringList = new ArrayList<>();
        if (isNotEmpty(list)) {
            for (Integer integer : list) {
                stringList.add(integer + "");
            }
        }
        return stringList;
    }

    public static String[] integerToStringArray(List<Integer> list) {
        List<String> stringList = integerToString(list);
        return stringList.toArray(new String[list.size()]);

    }

    public static Integer[] integerToArray(List<Integer> list) {
        return list.toArray(new Integer[list.size()]);

    }

    public static List<Integer> intArrayToList(Integer[] integers) {
        if (integers != null && integers.length > 0) {
            return Arrays.asList(integers);
        }
        return new ArrayList<>();
    }


    public static List<String> longToString(List<Long> list) {
        List<String> stringList = new ArrayList<>();
        if (isNotEmpty(list)) {
            for (Long l : list) {
                stringList.add(l + "");
            }
        }
        return stringList;
    }

    public static List<Integer> stringToInteger(List<String> list) {
        List<Integer> integerList = new ArrayList<>();
        if (isNotEmpty(list)) {
            for (String str : list) {
                if (StringUtil.isNotEmpty(str)) {
                    integerList.add(Integer.parseInt(str));
                }
            }
        }
        return integerList;
    }

    public static List<Long> stringToLong(List<String> list) {
        List<Long> longList = new ArrayList<>();
        if (isNotEmpty(list)) {
            for (String str : list) {
                if (StringUtil.isNotEmpty(str)) {
                    longList.add(Long.parseLong(str));
                }
            }
        }
        return longList;
    }

    /**
     * 分割List
     *
     * @param list     待分割的list
     * @param pageSize 每段list的大小
     * @return List<<               List               <               T>>
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        //list的大小
        int listSize = list.size();
        //页数
        int page = (listSize + (pageSize - 1)) / pageSize;
        //创建list数组 ,用来保存分割后的list
        List<List<T>> listArray = new ArrayList<>();
        //按照数组大小遍历
        for (int i = 0; i < page; i++) {
            //数组每一位放入一个分割后的list
            List<T> subList = new ArrayList<T>();
            //遍历待分割的list
            for (int j = 0; j < listSize; j++) {
                //当前记录的页码(第几页)
                int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
                //当前记录的页码等于要放入的页码时
                if (pageIndex == (i + 1)) {
                    //放入list中的元素到分割后的list(subList)
                    subList.add(list.get(j));
                }
                //当放满一页时退出当前循环
                if ((j + 1) == ((j + 1) * pageSize)) {
                    break;
                }
            }
            //将分割后的list放入对应的数组的位中
            listArray.add(subList);
        }
        return listArray;
    }

    public static <T> void repeatList(List<T> list) {
        Set<T> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
    }

    /**
     * @param firstArrayList  第一个ArrayList
     * @param secondArrayList 第二个ArrayList
     * @return resultList 交集ArrayList
     * @方法描述：获取两个ArrayList的交集
     */
    public static List<String> receiveCollectionList(List<String> firstArrayList, List<String> secondArrayList) {

        // 大集合用linkedlist
        LinkedList<String> result = new LinkedList<>(firstArrayList);
        // 小集合用hashset
        HashSet<String> othHash = new HashSet<>(secondArrayList);
        result.removeIf(s -> !othHash.contains(s));
        return new ArrayList<>(result);
    }
}
