package com.sp.framework.common.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.beans.BeanUtils.getPropertyDescriptors;


/**
 * Created by dell on 2017/9/25.
 */
public class BeanCopyUtil {
    /**
     * bean的属性拷贝工具类
     * @author alexlu
     * @version 1.0
     */
//        private static final Log logger = LogFactory.getLog(BeanCopyUtil.class);

    /**
     * bean嵌套
     */
    private static final String NESTED = ".";

    /**
     * 复制bean的属性（支持嵌套属性，以点号分割）
     *
     * @param source            拷贝属性的源对象
     * @param dest              拷贝属性的目的地对象
     * @param includeProperties 拷贝的属性列表
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IntrospectionException
     * @author alexlu
     * @date 2017-09-22
     */
    public static final void copyIncludeProperties(final Object source,
                                                   Object dest, final String[] includeProperties)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException,
            IntrospectionException {
        if (includeProperties == null || includeProperties.length == 0) {
            throw new IllegalArgumentException("未传入要拷贝的属性列表");
        }
        if (source == null) {
            throw new IllegalArgumentException("要拷贝的源对象为空");
        }
        if (dest == null) {
            throw new IllegalArgumentException("要拷贝的目的对象为空");
        }
        // 日志信息
//            if (logger.isTraceEnabled()) {
//                logger.trace("[source bean: " + source.getClass().getName() + " ]");
//                logger.trace("[destination bean: " + dest.getClass().getName()
//                        + " ]");
//            }
        // 拷贝
        for (String property : includeProperties) {
            PropertyDescriptor sourcePropertyDescriptor = null;
            PropertyDescriptor destPropertyDescriptor = null;
            if (isSimpleProperty(property)) { // 简单属性
                sourcePropertyDescriptor = getProperty(property, source);
                destPropertyDescriptor = getProperty(property, dest);
                if (sourcePropertyDescriptor == null) {
                    throw new IllegalArgumentException("要拷贝的源对象不存在该属性");
                }
                if (destPropertyDescriptor == null) {
                    throw new IllegalArgumentException("要拷贝到的目标对象不存在该属性");
                }

                if(sourcePropertyDescriptor.getValue(property) != null) {
                    copyProperty(property, source, dest);
                }
            } else { // 嵌套bean属性
                Object target = dest;
                Object realSource = source;
                String[] nestedProperty = getNestedProperty(property);
                if (nestedProperty != null && nestedProperty.length > 1) {
                    for (int i = 0; i < nestedProperty.length - 1; i++) {
                        sourcePropertyDescriptor = getProperty(
                                nestedProperty[i], realSource);
                        destPropertyDescriptor = getProperty(nestedProperty[i],
                                target);
                        if (sourcePropertyDescriptor == null) {
                            throw new IllegalArgumentException("要拷贝的源对象不存在该属性");
                        }
                        if (destPropertyDescriptor == null) {
                            throw new IllegalArgumentException(
                                    "要拷贝到的目标对象不存在该属性");
                        }
                        Method readMethod = sourcePropertyDescriptor
                                .getReadMethod();
                        realSource = readMethod.invoke(realSource);
                        readMethod = destPropertyDescriptor.getReadMethod();
                        Method writeMethod = destPropertyDescriptor
                                .getWriteMethod();
                        Object value = readMethod.invoke(target);
                        if (value == null) {
                            value = destPropertyDescriptor.getPropertyType()
                                    .newInstance();
                            writeMethod.invoke(target, value);
                        }
                        target = value;
                    }
                    final String prop = nestedProperty[nestedProperty.length - 1];
                    sourcePropertyDescriptor = getProperty(prop, realSource);
                    destPropertyDescriptor = getProperty(prop, target);
                    if (sourcePropertyDescriptor == null) {
                        throw new IllegalArgumentException("要拷贝的源对象不存在该属性");
                    }
                    if (destPropertyDescriptor == null) {
                        throw new IllegalArgumentException("要拷贝到的目标对象不存在该属性");
                    }
                    copyProperty(prop, realSource, target);
                }
            }
        }
    }

//    /**
//     * 复制bean的属性（支持嵌套属性，以点号分割）
//     *
//     * @param source            拷贝属性的源对象
//     * @param dest              拷贝属性的目的地对象
//     * @throws IntrospectionException
//     * @throws InvocationTargetException
//     * @throws IllegalAccessException
//     * @throws IllegalArgumentException
//     * @throws InstantiationException
//     * @throws ClassNotFoundException
//     * @throws IOException
//     * @author alexlu
//     * @date 2017-09-22
//     */
//    public static void copyProperties(final Object source, final Object dest) throws IntrospectionException,
//            IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException, InstantiationException, IOException,
//            ClassNotFoundException {
//        //final Object backupSource = clone(dest);
//
////        if (source == null) {
////            throw new IllegalArgumentException("要拷贝的源对象为空");
////        }
////        if (dest == null) {
////            throw new IllegalArgumentException("要拷贝的目的对象为空");
////        }
//        org.apache.commons.beanutils.BeanUtils.copyProperties(dest, source);
//        // 还原排除的属性值
//        //revertProperties(backupSource, dest, excludeProperties);
//    }

    /**
     * 从备份对象中还原属性
     *
     * @param backup     备份bean
     * @param target     目标bean
     * @param properties 属性列表
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws IntrospectionException
     * @throws InstantiationException
     * @author alexlu
     * @date 2017-09-22
     */
    private static void revertProperties(final Object backup, Object target,
                                         final String... properties) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            IntrospectionException, InstantiationException {
        if (properties == null || properties.length == 0) {
            return;
        }
        if (backup == null) {
            throw new IllegalArgumentException("备份对象为空");
        }
        if (target == null) {
            throw new IllegalArgumentException("目的对象为空");
        }
//            // 日志信息
//            if (logger.isTraceEnabled()) {
//                logger.trace("[source bean: " + backup.getClass().getName() + " ]");
//                logger.trace("[destination bean: " + target.getClass().getName()
//                        + " ]");
//            }

        // 拷贝
        for (String property : properties) {
            PropertyDescriptor sourcePropertyDescriptor = null;
            PropertyDescriptor destPropertyDescriptor = null;
            if (isSimpleProperty(property)) { // 简单属性
                sourcePropertyDescriptor = getProperty(property, backup);
                destPropertyDescriptor = getProperty(property, target);
                if (sourcePropertyDescriptor == null) {
                    throw new IllegalArgumentException("要拷贝的源对象不存在该属性");
                }
                if (destPropertyDescriptor == null) {
                    throw new IllegalArgumentException("要拷贝到的目标对象不存在该属性");
                }
                copyProperty(property, backup, target);
            } else { // 嵌套bean属性
                Object targetObj = target;
                Object realBackup = backup;
                String[] nestedProperty = getNestedProperty(property);
                if (nestedProperty != null && nestedProperty.length > 1) {
                    for (int i = 0; i < nestedProperty.length - 1; i++) {
                        sourcePropertyDescriptor = getProperty(
                                nestedProperty[i], realBackup);
                        destPropertyDescriptor = getProperty(nestedProperty[i],
                                targetObj);
                        if (sourcePropertyDescriptor == null) {
                            throw new IllegalArgumentException("要拷贝的源对象不存在该属性");
                        }
                        if (destPropertyDescriptor == null) {
                            throw new IllegalArgumentException(
                                    "要拷贝到的目标对象不存在该属性");
                        }
                        Method readMethod = sourcePropertyDescriptor
                                .getReadMethod();
                        realBackup = readMethod.invoke(realBackup);
                        if (realBackup == null) { // 判断备份对象嵌套属性是否为空
                            realBackup = sourcePropertyDescriptor
                                    .getPropertyType().newInstance();
                        }
                        Method writeMethod = destPropertyDescriptor
                                .getWriteMethod();
                        readMethod = destPropertyDescriptor.getReadMethod();
                        Object value = readMethod.invoke(targetObj);
                        if (value == null) {
                            value = destPropertyDescriptor.getPropertyType()
                                    .newInstance();
                            writeMethod.invoke(targetObj, value);
                        }
                        targetObj = value;
                    }
                    final String prop = nestedProperty[nestedProperty.length - 1];
                    sourcePropertyDescriptor = getProperty(prop, realBackup);
                    destPropertyDescriptor = getProperty(prop, targetObj);
                    if (sourcePropertyDescriptor == null) {
                        throw new IllegalArgumentException("要拷贝的源对象不存在该属性");
                    }
                    if (destPropertyDescriptor == null) {
                        throw new IllegalArgumentException("要拷贝到的目标对象不存在该属性");
                    }
                    copyProperty(prop, realBackup, targetObj);
                }
            }
        }
    }

    /**
     * 对象克隆
     *
     * @author alexlu
     * @date 2017-09-22
     */
    private static Object clone(final Object value) throws IOException,
            ClassNotFoundException {
        // 字节数组输出流，暂存到内存中
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // 序列化
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(value);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        // 反序列化
        return ois.readObject();
    }

    /**
     * 判断是否为简单属性，是:返回ture
     *
     * @author alexlu
     * @date 2017-09-22
     */
    private static boolean isSimpleProperty(final String property) {
        return !property.contains(NESTED);
    }

    /**
     * 获取目标bean的属性
     *
     * @author alexlu
     * @date 2017-09-22
     */
    private static PropertyDescriptor getProperty(final String propertyName,
                                                  final Object target) {
        if (target == null) {
            throw new IllegalArgumentException("查询属性的对象为空");
        }
        if (propertyName == null || "".equals(propertyName)) {
            throw new IllegalArgumentException("查询属性不能为空值");
        }
        PropertyDescriptor propertyDescriptor = null;
        try {
            propertyDescriptor = new PropertyDescriptor(propertyName,
                    target.getClass());
        } catch (IntrospectionException e) {
            //logger.info("不存在该属性");
            return null;
        }
        return propertyDescriptor;
    }

    /**
     * 单个属性复制--原数据源和目的数据源必须要有该属性方可
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws IntrospectionException
     * @author alexlu
     * @date 2017-09-22
     */
    public static void copyProperty(final String propertyName,
                                    final Object source, Object dest) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            IntrospectionException {
        PropertyDescriptor property;
        property = new PropertyDescriptor(propertyName, source.getClass());
        Method getMethod = property.getReadMethod();
        Object value = getMethod.invoke(source);
        property = new PropertyDescriptor(propertyName, dest.getClass());
        Method setMethod = property.getWriteMethod();
        setMethod.invoke(dest, value);
    }

    /**
     * 获取嵌套Bean的属性
     *
     * @author alexlu
     * @date 2017-09-22
     */
    public static String[] getNestedProperty(final String nestedProperty) {
        if (nestedProperty == null || "".equals(nestedProperty)) {
            new IllegalArgumentException("参数为空值");
        }
        return nestedProperty.split("\\" + NESTED);
    }

    /**
     * Spring Data JPA 更新属性值到数据库
     * 原理就是：调用newObject的getOOXX方法获取该值，判断不为空的话，调用rawObject的setOOXX方法，实现改变rawObject的值
     * @param rawObject
     * @param newObject
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static boolean updateNotNullField(Object rawObject, Object newObject, String... ignoreBaseMethodNames)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //如果连个对象不一致。不进行更新字段值的操作
        if (rawObject.getClass().getName() != newObject.getClass().getName()) {
            return false;
        }
        //获取原始对象中的所有public方法
        Method[] methods = rawObject.getClass().getDeclaredMethods();
        //用于提取不包含指定关键词的方法
        List<String> ignoreList = ignoreBaseMethodNames != null? Arrays.asList(ignoreBaseMethodNames):null;
        String regStr = "SerialVersionUID";
        if (!CollectionUtils.isEmpty(ignoreList)) {
            for (String baseName : ignoreList) {
                regStr += "|" + baseName;
            }
        }
        String regExpression = "^(get)(?!"+ regStr +")(\\w+)";
        Pattern pattern = Pattern.compile(regExpression);
        Matcher m;
        for (Method method : methods) {
            m = pattern.matcher(method.getName());
            //正则匹配以get开头，后面不能匹配Id、CreateAt这两个单词的方法
            if (m.find()) {
                Object newValue = method.invoke(newObject, null);
                //忽略值为空的字段
                if ((newValue == null) ) {
                    continue;
                }else if ((newValue instanceof String) && StringUtils.isEmpty(newValue)) {
                    continue;
                } else if ((newValue instanceof Integer) && newValue.equals(0)) {
                    continue;
                } else if ((newValue instanceof Long) && newValue.equals(0)) {
                    continue;
                }
                //取出get方法名后面的字段名
                String fieldName = m.group(2);
                //找到该字段名的set方法
                Method rawMethod = rawObject.getClass().getMethod("set" + fieldName, method.getReturnType());
                //调用实体对象的set方法更新字段值
                rawMethod.invoke(rawObject, newValue);
            }
        }
        return true;
    }

    /**
     * 对象转Map，不进行驼峰转下划线，不忽略值为空的字段
     *
     * @param <T> Bean类型
     * @param bean bean对象
     * @return Map
     */
    public static <T> Map<String, Object> beanToMap(T bean) throws Exception {
        return beanToMap(bean, false, false);
    }

    /**
     * 对象转Map
     *
     * @param <T> Bean类型
     * @param bean bean对象
     * @param isToUnderlineCase 是否转换为下划线模式
     * @param ignoreNullValue 是否忽略值为空的字段
     * @return Map
     */
    public static <T> Map<String, Object> beanToMap(T bean, boolean isToUnderlineCase, boolean ignoreNullValue)
            throws Exception {

        if (bean == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(bean.getClass());
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (false == key.equals("class") && false == key.equals("declaringClass")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);
                    if (false == ignoreNullValue || (null != value && false == value.equals(bean))) {
                        map.put(isToUnderlineCase ? StringUtil.toUnderlineCase(key) : key, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Convert bean to map failed!", e);
        }
        return map;
    }
}
