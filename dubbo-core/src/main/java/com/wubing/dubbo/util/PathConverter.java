package com.wubing.dubbo.util;

/**
 * 扫描路径转换
 * <p>
 * 将包路径转换成系统路径
 *
 * @author: WB
 * @version: v1.0
 */
public class PathConverter {
    private PathConverter() {
    }

    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:/";

    public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    public static final String Separator = "/";

    /**
     * 转换包路径
     *
     * @return
     */
    public static String converter(String basePackage) {
        return CLASSPATH_ALL_URL_PREFIX + basePackage.replace(".", Separator) + Separator + DEFAULT_RESOURCE_PATTERN;
    }

}
