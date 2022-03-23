package com.wubing.dubbo.util;

/**
 * SpringBean相关工具类
 *
 * @author: WB
 * @version: v1.0
 */
public class BeanUtils {

    /**
     * 根据class名称获取对应的BeanName（首字母小写）
     */
    public static String getName(Class<?> beanClass) {
        String name = beanClass.getName();
        String substring = name.substring(name.lastIndexOf(".") + 1);
        return substring.substring(0, 1).toLowerCase() + substring.substring(1, substring.length());
    }

    public static String getPackage(Class<?> beanClass) {
        return getPackage(beanClass.getName());
    }

    public static String getPackage(String className) {
        return className.substring(0, className.lastIndexOf("."));
    }


}