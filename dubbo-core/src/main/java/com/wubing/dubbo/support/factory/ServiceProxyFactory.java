package com.wubing.dubbo.support.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 自定义注解
 * <p>
 * 对目标接口生成代理对象工厂
 *
 * @author: WB
 * @version: v1.0
 */
public class ServiceProxyFactory implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> className) {
        return applicationContext.getBean(className);
    }

}
