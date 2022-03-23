package com.wubing.configuration;

import com.wubing.dubbo.support.factory.ServiceProxyFactory;
import com.wubing.dubbo.marker.ConfigurationMarker;
import com.wubing.dubbo.marker.DubboClientMarker;
import com.wubing.dubbo.marker.DubboServerMarker;
import com.wubing.dubbo.protocol.client.NettyClient;
import com.wubing.dubbo.protocol.server.NettyServer;
import com.wubing.dubbo.support.processor.DubboReferenceAnnotationBeanPostProcessor;
import com.wubing.dubbo.support.registrar.RedisServiceRegistry;
import com.wubing.properties.DubboProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

/**
 * 自动配置类
 *
 * @author: WB
 * @version: v1.0
 */
@Configuration
@ConditionalOnBean(ConfigurationMarker.class)
@EnableConfigurationProperties(DubboProperties.class)
@Import(DubboReferenceAnnotationBeanPostProcessor.class)
public class DubboAutoConfiguration {
    public DubboAutoConfiguration() {
    }

    @ConditionalOnBean(DubboClientMarker.class)
    private static class DubboReferenceConfiguration {
        public DubboReferenceConfiguration() {
        }

        @Bean("redisClientRegister")
        public RedisServiceRegistry redisClientRegister(DubboProperties dubboProperties) {
            return new RedisServiceRegistry(dubboProperties.getRegisterHost(), dubboProperties.getRegisterPort());
        }

        @Bean("dubboRpcClient")
        public NettyClient dubboRpcClient(DubboProperties dubboProperties) {
            return new NettyClient(dubboProperties.getHost(), dubboProperties.getPort());
        }
    }

    @ConditionalOnBean(DubboServerMarker.class)
    private static class DubboServiceConfiguration {
        public DubboServiceConfiguration() {
        }

        @Bean("redisServerRegister")
        public RedisServiceRegistry redisServerRegister(DubboProperties dubboProperties) {
            return new RedisServiceRegistry(dubboProperties.getRegisterHost(), dubboProperties.getRegisterPort());
        }

        @Bean
        public ServiceProxyFactory serviceProxyFactory() {
            return new ServiceProxyFactory();
        }

        @Bean("dubboRpcServer")
        @DependsOn("redisServerRegister")
        public NettyServer dubboRpcServer(DubboProperties dubboProperties) {
            return new NettyServer(dubboProperties.getHost(), dubboProperties.getPort());
        }
    }

}
