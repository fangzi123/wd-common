package com.wdcloud.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.wdcloud.base.exception.BaseException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.ReflectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public class MapUtil {

    /**
     * Map key 排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> order(Map<String, String> map) {
        HashMap<String, String> tempMap = new LinkedHashMap<>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());

        infoIds.sort(Comparator.comparing(Map.Entry::getKey));

        for (Map.Entry<String, String> item : infoIds) {
            tempMap.put(item.getKey(), item.getValue());
        }
        return tempMap;
    }


    /**
     * 转换对象为map
     *
     * @param object 对象
     * @param ignore 忽略属性
     * @return
     */
    public static Map<String, String> objectToMap(Object object, String... ignore) {
        Map<String, String> tempMap = new LinkedHashMap<>();
        for (Field field : getAllFields(object.getClass())) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            boolean ig = false;
            if (ignore != null && ignore.length > 0) {
                for (String i : ignore) {
                    ig = i.equals(field.getName());
                    if (ig) {
                        break;
                    }
                }
            }
            if (ig) {
                continue;
            } else {
                Object o = null;
                try {
                    o = field.get(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!field.getName().equals("serialVersionUID") && !field.getName().equals("FAIL")
                        && !field.getName().equals("OKMSG") && !field.getName().equals("SUCCESS") && !field.getName().equals("hmac")) {
                    XmlElement annotation = field.getAnnotation(XmlElement.class);
                    if (annotation == null) {
                        tempMap.put(field.getName(), o == null ? "" : o.toString());
                    } else {
                        String name = annotation.name();
                        if (StringUtil.isNotEmpty(name) && !StringUtil.stringEquals(name, "##default")) {
                            tempMap.put(name, o == null ? "" : o.toString());
                        } else {
                            tempMap.put(field.getName(), o == null ? "" : o.toString());
                        }

                    }
                }
            }
        }
        return tempMap;
    }

    /**
     * 获取所有Fields,包含父类field
     *
     * @param clazz
     * @return
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (!clazz.equals(Object.class)) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            List<Field> fields2 = getAllFields(clazz.getSuperclass());
            if (fields2 != null) {
                fields.addAll(fields2);
            }
        }
        return fields;
    }

    /**
     * url 参数串连
     *
     * @param map            参数
     * @param keyLower       是否全部小写
     * @param valueUrlEncode 是否编码
     * @return
     */
    public static String mapJoin(Map<String, String> map, boolean keyLower, boolean valueUrlEncode) {
        return mapJoin(map, keyLower, valueUrlEncode, "UTF-8");
    }

    /**
     * url 参数串连
     *
     * @param map            参数
     * @param keyLower       是否全部小写
     * @param valueUrlEncode 是否编码
     * @return
     */
    public static String mapJoin(Map<String, String> map, boolean keyLower, boolean valueUrlEncode, String encodeCharset) {
        StringBuilder stringBuilder = new StringBuilder();

        map.entrySet().stream().filter(paramEntry -> StringUtil.isNotEmpty(paramEntry.getValue())).forEach(paramEntry -> {
            try {
                String tempKey = (paramEntry.getKey().endsWith("_") && paramEntry.getKey().length() > 1) ?
                        paramEntry.getKey().substring(0, paramEntry.getKey().length() - 1) : paramEntry.getKey();
                stringBuilder.append(keyLower ? tempKey.toLowerCase() : tempKey)
                        .append("=")
                        .append(valueUrlEncode ? URLEncoder.encode(paramEntry.getValue(), encodeCharset).replace("+", "%20") : paramEntry.getValue())
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * 简单 xml 转换为 Map
     *
     * @param xml xml串
     * @return
     */
    public static Map<String, String> xmlToMap(String xml) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getChildNodes();
            Map<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                map.put(e.getNodeName(), e.getTextContent());
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 排序、计算md5
     *
     * @param map         map
     * @param paternerKey
     * @return
     */
    public static String generateSign(Map<String, String> map, String paternerKey) {
        Map<String, String> tmap = MapUtil.order(map);
        if (tmap.containsKey("sign")) {
            tmap.remove("sign");
        }
        String str = MapUtil.mapJoin(tmap, false, false);
        return DigestUtils.md5Hex(str + "&key=" + paternerKey).toUpperCase();
    }

    /**
     * 排序、计算md5
     *
     * @param obj         对象
     * @param paternerKey
     * @return
     */
    public static <T> String generateSign(T obj, String paternerKey, String... ignore) {
        Map<String, String> map = objectToMap(obj, ignore);
        Map<String, String> tmap = MapUtil.order(map);
        if (tmap.containsKey("sign")) {
            tmap.remove("sign");
        }
        String str = MapUtil.mapJoin(tmap, false, false);
        return DigestUtils.md5Hex(str + "&key=" + paternerKey).toUpperCase();
    }

    public static <T> String generateSha256Sign(T target, String key, String... ignore) {
        Map<String, String> orderParam = MapUtil.order(objectToMap(target, ignore));
        if (orderParam.containsKey("sign")) {
            orderParam.remove("sign");
        }
        String paramStr = MapUtil.mapJoin(orderParam, false, false) + "&key=" + key;
        try {
            byte[] digest = DigestUtils.getSha256Digest().digest(paramStr.getBytes("utf-8"));
            return new String(digest, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据注解@JSONField来转换对象到Map<String, String>，不处理嵌套复杂的对象
     * 值为null时，结果中不会保留
     *
     * @param object
     * @return
     */
    public static Map<String, String> objectToMapByJsonAnno(Object object) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(object), Map.class);
        Map<String, String> result = new HashMap<>(map.size());
        map.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .forEach(entry -> result.put(entry.getKey(), entry.getValue().toString()));

        return result;
    }

    public static Map<String, String> parseUrlParams(String urlParam, String valueCharset) {
        Map<String, String> map = new HashMap<>();
        if (StringUtil.isEmpty(urlParam)) {
            return map;
        }
        String[] params = urlParam.split("&");
        for (String param : params) {
            String[] p = param.split("=");
            if (p.length == 2) {
                try {
                    map.put(p[0], URLDecoder.decode(p[1], valueCharset));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return map;
    }

    /**
     * 解析map形式参数到对象中，只支持基本类型、字符串、日期类型属性值转换
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T parseObject(Map<String, String> source, Class<T> targetClass) {
        try {
            // fixme
            return JSON.parseObject(JSON.toJSONString(source), targetClass);
        } catch (JSONException e) {
            throw new BaseException(e);
        }
    }
}
