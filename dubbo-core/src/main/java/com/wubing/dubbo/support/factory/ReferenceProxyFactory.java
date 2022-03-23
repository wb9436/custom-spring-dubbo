package com.wubing.dubbo.support.factory;

import com.wubing.dubbo.protocol.messge.RequestMessage;
import com.wubing.dubbo.protocol.client.NettyClient;

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
public class ReferenceProxyFactory<T> {
    private static final ClassLoader classLoader = ReferenceProxyFactory.class.getClassLoader();

    public static <T> T getProxyInstance(Class<T> referenceInterface) {
        Object object = Proxy.newProxyInstance(classLoader,
                new Class[]{referenceInterface},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String className = referenceInterface.getName();
                        RequestMessage message = new RequestMessage(className, method.getName(), method.getParameterTypes(), args);
                        return NettyClient.sendMessage(message);
                    }
                }
        );
        return referenceInterface.cast(object);
    }

}
