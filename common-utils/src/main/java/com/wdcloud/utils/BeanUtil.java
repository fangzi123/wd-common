package com.wdcloud.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csf on 2015/5/16.
 *
 * @author csf
 */
@SuppressWarnings(value = "unused")
public class BeanUtil {
    private static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    public static final String SORT_DESC = "desc"; //排序方式

    public static boolean checkObject(Object obj, boolean strNotEmpty, String...checkAttribute) {
        Assert.notNull(obj, "对象不能为空");

        if (checkAttribute == null) {
            return true;
        }

        Class cls = obj.getClass();
        while (true) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                for (String attribute : checkAttribute) {
                    if (field.getName().equalsIgnoreCase(attribute)) {
                        if (checkValueIsNull(field, obj, strNotEmpty)) {
                            return false;
                        }
                    }
                }
            }
            if (cls == Object.class) {
                break;
            }

            cls = cls.getSuperclass();
        }

        return true;
    }

    /**
     * 用来遍历对象属性和属性值来判断对象属性是否为空
     *
     * @param obj            传入对象
     * @param checkAttribute 判断的属性(不区分大小写)
     * @return 返回校验结果 如果符合标签，返回true
     */
    public static boolean checkObject(Object obj, String... checkAttribute) {
        Assert.notNull(obj, "对象不能为空");

        if (checkAttribute == null) {
            return true;
        }

        Class cls = obj.getClass();
        while (true) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                for (String attribute : checkAttribute) {
                    if (field.getName().equalsIgnoreCase(attribute)) {
                        if (checkValueIsNull(field, obj, false)) { //如果属性为空
                            return false;
                        }
                    }
                }
            }
            if (cls == Object.class) {
                break;
            }

            cls = cls.getSuperclass();
        }

        return true;
    }

    /**
     * 检查字段是否为空
     *
     * @param field 属性
     * @param obj   对象
     * @param checkEmpty 如果字段类型为String, 检查是否为空白字符串
     * @return 如果属性为空，返回True
     */
    private static boolean checkValueIsNull(Field field, Object obj, boolean checkEmpty) {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            Object val = field.get(obj);
            return (val instanceof String) && checkEmpty && StringUtil.isEmpty((String) val) || val == null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(accessible);
        }
        return false;
    }

    /**
     * 统计不为空的属性
     *
     * @param obj 统计对象
     * @return 结果
     */
    public static <T> int countNotNullValues(T obj) {
        int count = 0;
        if (obj != null) {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (countNotNullValues(field, obj)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean countNotNullValues(Field field, Object obj) {
        assert field != null && obj != null;
        try {
            //获取属性的类型(有需要，自己增加)
            Object o = field.get(obj);
            if (o != null) {
                return true;
            }
        } catch (Exception e) {
            logger.error(field.getName() + "获取值失败");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将List中的对象拷贝到目标对象的List中(标准Bean)
     *
     * @param sourceList 源List<T>
     * @param targetCls  目标对象类型
     * @param <T>        源类型
     * @param <R>        目标类型
     * @return 目标类型List数组
     */
    public static <T, R> List<R> beanCopyPropertiesForList(List<T> sourceList, Class<R> targetCls) {
        Assert.notNull(targetCls, "target class can not null");
        List<R> targetList = new ArrayList<R>();
        if (sourceList != null && !sourceList.isEmpty()) {
            for (T source : sourceList) {
                targetList.add(beanCopyProperties(source, targetCls));
            }
        }
        return targetList;
    }

    /**
     * 属性值拷贝
     *
     * @param source    源对象
     * @param targetCls 目标对象类
     * @Return 拷贝目标类的实体
     */
    public static <R> R copyProperties(Object source, Class<R> targetCls) {
        Assert.notNull(targetCls, "target class can not null");
        try {
            R target = targetCls.getDeclaredConstructor().newInstance();
            if (source != null) {
                copyProperties(source, target, false);
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 属性值拷贝(标准Bean)
     *
     * @param source    源对象
     * @param targetCls 目标对象类
     * @Return 拷贝目标类的实体
     */
    public static <R> R beanCopyProperties(Object source, Class<R> targetCls) {
        try {
            Assert.notNull(targetCls, "target class can not null");
            R target = targetCls.getDeclaredConstructor().newInstance();
            if (source != null) {
                BeanUtils.copyProperties(source, target);
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 属性值拷贝(标准Bean)
     *
     * @param source    源对象
     * @param targetCls 目标对象类
     * @return 拷贝目标类的实体
     */
    public static <R> R beanCopyProperties(Object source, Class<R> targetCls, String ignoreProperties) {
        try {
            Assert.notNull(targetCls, "target class can not null");
            R target = targetCls.getDeclaredConstructor().newInstance();
            if (source != null) {
                if (StringUtil.isEmpty(ignoreProperties)) {
                    BeanUtils.copyProperties(source, target);
                } else {
                    BeanUtils.copyProperties(source, target, ignoreProperties.split(","));
                }
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 属性值拷贝(标准Bean)
     *
     * @param source 源对象
     * @param target 目标对象
     * @return 拷贝目标类的实体
     */
    public static void beanCopyProperties(Object source, Object target, String ignoreProperties) {
        try {
            Assert.notNull(target, "target can not null");
            if (source != null) {
                if (StringUtil.isEmpty(ignoreProperties)) {
                    BeanUtils.copyProperties(source, target);
                } else {
                    BeanUtils.copyProperties(source, target, ignoreProperties.split(","));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <R> R copyPropertiesBySpring(Object source, Class<R> targetClass) {
        try {
            R target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将属性一样的两个类，进行属性值拷贝
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, true);
    }

    /**
     * 将属性一样的两个类，进行属性值拷贝,值为空时，不进行拷贝
     *
     * @param source             源对象
     * @param target             目标对象
     * @param attributeCoverFlag 当为true时，属性值为空时也进行覆盖
     */
    public static void copyProperties(Object source, Object target, boolean attributeCoverFlag) {
        if (source != null && target != null) {
            Field[] sourceFields = source.getClass().getDeclaredFields();
            Field[] targetFields = target.getClass().getDeclaredFields();
            for (Field sourceField : sourceFields) {
                for (Field targetField : targetFields) {
                    if (targetField.getName().equalsIgnoreCase("serialVersionUID")) {
                        continue;
                    }
                    if (sourceField.getName().equals(targetField.getName()) && sourceField.getType().equals(targetField.getType())) {
                        sourceField.setAccessible(true);
                        targetField.setAccessible(true);
                        try {
                            Object value = sourceField.get(source);
                            if (attributeCoverFlag) {
                                targetField.set(target, value);
                            } else if (value != null) {
                                targetField.set(target, value);
                            }
                        } catch (IllegalAccessException e) {
                            logger.error(target.getClass() + " 中 " + targetField.getName() + " 赋值错误");
                            throw new RuntimeException(target.getClass() + " 中 " + targetField.getName() + " 赋值错误");
                        }
                    }
                }
            }
        }
    }

    /**
     * Get value or default value if it is null.
     *
     * @param value        原值
     * @param defaultValue 默认值
     * @param <T>          类型
     * @return 结果
     */
    public static <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 将属性一样的两个类，进行属性值拷贝,值为空时，不进行拷贝
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesWithOutNull(Object source, Object target) {
        if (source != null && target != null) {
            Field[] sourceFields = source.getClass().getDeclaredFields();
            Field[] targetFields = target.getClass().getDeclaredFields();
            for (Field sourceField : sourceFields) {
                for (Field targetField : targetFields) {
                    if (sourceField.getName().equals(targetField.getName()) && sourceField.getType().equals(targetField.getType())) {
                        sourceField.setAccessible(true);
                        targetField.setAccessible(true);
                        try {
                            Object value = sourceField.get(source);
                            if (value != null) {
                                targetField.set(target, value);
                            }
                        } catch (IllegalAccessException e) {
                            logger.error(target.getClass() + " 中 " + targetField.getName() + " 赋值错误");
                        }
                    }
                }
            }
        }
    }

    /**
     * Return the named getter method on the bean or null if not found.
     *
     * @param bean
     * @param propertyName
     * @return the named getter method
     */
    private static Method getMethod(Object bean, String propertyName) {
        StringBuilder sb = new StringBuilder("get").append(Character.toUpperCase(propertyName.charAt(0)));
        if (propertyName.length() > 1) {
            sb.append(propertyName.substring(1));
        }
        String getterName = sb.toString();
        for (Method m : bean.getClass().getMethods()) {
            if (getterName.equals(m.getName()) && m.getParameterTypes().length == 0) {
                return m;
            }
        }
        return null;
    }

    /**
     * Return the named field on the bean or null if not found.
     *
     * @param bean
     * @param propertyName
     * @return the named field
     */
    private static Field getField(Object bean, String propertyName) {
        for (Field f : bean.getClass().getDeclaredFields()) {
            if (propertyName.equals(f.getName())) {
                return f;
            }
        }
        return null;
    }

    private static void validateArgs(Object bean, String propertyName) {
        if (bean == null) {
            throw new IllegalArgumentException("bean is null");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }
        if (propertyName.trim().length() == 0) {
            throw new IllegalArgumentException("propertyName is empty");
        }
    }

    /**
     * Retrieve a named bean property value.
     *
     * @param bean bean
     * @param propertyName
     * @return the property value
     */
    public static Object getBeanProperty(Object bean, String propertyName) {
        validateArgs(bean, propertyName);

        // try getters first
        Method getter = getMethod(bean, propertyName);
        if (getter != null) {
            try {
                return getter.invoke(bean);
            } catch (Exception e) {
            }
        }

        Field field = getField(bean, propertyName);
        if (field != null) {
            try {
                field.setAccessible(true);
                return field.get(bean);
            } catch (Exception e) {
            }
        }

        return null;
    }

    /**
     * Retrieve a Long bean property value.
     *
     * @param bean bean
     * @param propertyName
     * @return long value
     * @throws NoSuchFieldException
     */
    public static long getLongBeanProperty(final Object bean, final String propertyName) throws NoSuchFieldException {
        validateArgs(bean, propertyName);
        Object o = getBeanProperty(bean, propertyName);
        if (o == null) {
            throw new NoSuchFieldException(propertyName);
        } else if (!(o instanceof Number)) {
            throw new IllegalArgumentException(propertyName + " not an Number");
        }
        return ((Number)o).longValue();
    }

}
