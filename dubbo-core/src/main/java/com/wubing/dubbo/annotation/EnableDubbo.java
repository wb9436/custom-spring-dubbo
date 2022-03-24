package com.wubing.dubbo.annotation;

import com.wubing.dubbo.support.marker.ConfigurationMarker;
import com.wubing.dubbo.support.DubboServiceScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 * <p>
 * 控制自定义注解开启
 *
 * @author: WB
 * @version: v1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ConfigurationMarker.class, DubboServiceScannerRegistrar.class})
public @interface EnableDubbo {

    String[] basePackages() default {};
}
