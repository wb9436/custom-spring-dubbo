package com.wubing.dubbo.support.proxy;

import com.wubing.dubbo.protocol.messge.RequestMessage;
import com.wubing.dubbo.protocol.client.NettyClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 自定义注解
 * <p>
 * 对目标接口生成代理对象工厂
 *
 * @author: WB
 * @version: v1.0
 */
@Slf4j
public class ReferenceProxyFactory<T> {
    private static final ClassLoader classLoader = ReferenceProxyFactory.class.getClassLoader();

    public static <T> T getProxyInstance(Class<T> referenceInterface) {
        Object object = Proxy.newProxyInstance(classLoader,
                new Class[]{referenceInterface},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        long startTime = System.currentTimeMillis();
                        String className = referenceInterface.getName();
                        RequestMessage message = new RequestMessage(className, method.getName(), method.getParameterTypes(), args);
                        Object result = NettyClient.sendMessage(message);
                        log.debug("本次请求：{}，共计耗时：{}ms", method.getName(), System.currentTimeMillis() - startTime);
                        return result;
                    }
                }
        );
        return referenceInterface.cast(object);
    }

}
