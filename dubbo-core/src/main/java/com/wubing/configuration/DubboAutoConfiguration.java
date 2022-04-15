package com.wubing.configuration;

import com.wubing.dubbo.support.proxy.ServiceProxyFactory;
import com.wubing.dubbo.support.marker.ConfigurationMarker;
import com.wubing.dubbo.support.marker.DubboClientMarker;
import com.wubing.dubbo.support.marker.DubboServerMarker;
import com.wubing.dubbo.protocol.client.NettyClient;
import com.wubing.dubbo.protocol.server.NettyServer;
import com.wubing.dubbo.support.DubboReferenceAnnotationBeanPostProcessor;
import com.wubing.dubbo.support.register.RedisServiceRegister;
import com.wubing.properties.DubboProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        public RedisServiceRegister redisClientRegister(DubboProperties dubboProperties) {
            return new RedisServiceRegister(dubboProperties.getRegisterHost(), dubboProperties.getRegisterPort());
        }

        @Bean
        public ExecutorService clientSingleExecutor() {
            return Executors.newSingleThreadExecutor();
        }

        @Bean("dubboRpcClient")
        public NettyClient dubboRpcClient(DubboProperties dubboProperties, ExecutorService clientSingleExecutor) {
            NettyClient nettyClient = new NettyClient(dubboProperties.getHost(), dubboProperties.getPort());
            clientSingleExecutor.execute(nettyClient);
            return nettyClient;
        }
    }

    @ConditionalOnBean(DubboServerMarker.class)
    private static class DubboServiceConfiguration {
        public DubboServiceConfiguration() {
        }

        @Bean("redisServerRegister")
        public RedisServiceRegister redisServerRegister(DubboProperties dubboProperties) {
            return new RedisServiceRegister(dubboProperties.getRegisterHost(), dubboProperties.getRegisterPort());
        }

        @Bean
        public ServiceProxyFactory serviceProxyFactory() {
            return new ServiceProxyFactory();
        }

        @Bean
        public ExecutorService serverSingleExecutor() {
            return Executors.newSingleThreadExecutor();
        }

        @Bean("dubboRpcServer")
        @DependsOn("redisServerRegister")
        public NettyServer dubboRpcServer(DubboProperties dubboProperties, ExecutorService serverSingleExecutor) {
            NettyServer nettyServer = new NettyServer(dubboProperties.getHost(), dubboProperties.getPort());
            serverSingleExecutor.execute(nettyServer);
            return nettyServer;
        }
    }

}
