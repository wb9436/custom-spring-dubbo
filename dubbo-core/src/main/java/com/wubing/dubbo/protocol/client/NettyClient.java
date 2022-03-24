package com.wubing.dubbo.protocol.client;

import com.wubing.dubbo.protocol.messge.MessagePromise;
import com.wubing.dubbo.protocol.messge.RequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;

/**
 * Netty客户端
 *
 * @author: WB
 * @version: v1.0
 */
public class NettyClient {
    private static final Log logger = LogFactory.getLog(NettyClient.class);
    private static final ClassLoader CLASS_LOADER = NettyClient.class.getClassLoader();
    private static Channel channel;

    public NettyClient(String host, int port) {
        logger.info("NettyClient init ...");
        this.init(host, port);
    }

    private void init(String host, int port) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(CLASS_LOADER)));
                        ch.pipeline().addLast(new ObjectEncoder());
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                }).connect(new InetSocketAddress(host, port));
        try {
            channel = channelFuture.sync().channel();
            //异步断开，避免阻塞主线程
            channel.closeFuture().addListener(future -> {
                worker.shutdownGracefully();
            });
            logger.info("Netty 客户端连接成功，连接地址为：" + host + ":" + port);
        } catch (InterruptedException e) {
            logger.error("Netty 服务端异常，已断开连接。。。");
        }
    }

    public static Object sendMessage(RequestMessage message) {
        MessagePromise promise = new MessagePromise(message.getMessageId());
        try {
            channel.writeAndFlush(message);
            //阻塞等待响应
            promise.waiting();
            //判断请求结果
            if (promise.isSuccess()) {
                return promise.getResult();
            } else {
                throw new RuntimeException(promise.getFailure());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
