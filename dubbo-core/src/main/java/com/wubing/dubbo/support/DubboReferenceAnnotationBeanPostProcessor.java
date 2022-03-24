package com.wubing.dubbo.support;

import com.wubing.dubbo.annotation.DubboReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * 自定义注解
 * <p>
 * 对自定义@DubboReference实现依赖注入功能
 *
 * @author: WB
 * @version: v1.0
 */
public class DubboReferenceAnnotationBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            // 针对@DubboReference的属性进行依赖注入
            if (field.getAnnotation(DubboReference.class) != null) {
                try {
                    // 从容器中获取属性的Bean对象
                    Object valueBean = context.getBean(field.getName(), field.getType());
                    if (valueBean == null) {
                        throw new IllegalAccessException("@DubboReference annotation annotated field cannot get value from context");
                    }
                    field.setAccessible(true);
                    field.set(bean, valueBean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
}
