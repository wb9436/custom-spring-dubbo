package com.wubing.dubbo.protocol.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;

/**
 * Netty服务端
 *
 * @author: WB
 * @version: v1.0
 */
public class NettyServer implements Runnable {
    private static final Log logger = LogFactory.getLog(NettyServer.class);
    private static final ClassLoader CLASS_LOADER = NettyServer.class.getClassLoader();

    private String host;
    private Integer port;

    public NettyServer(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    private void connect() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(CLASS_LOADER)));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }).bind(new InetSocketAddress(host, port));

            Channel channel = channelFuture.sync().channel();
            logger.info("Netty Server启动成功：" + host + ":" + port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public void run() {
        connect();
    }
}
