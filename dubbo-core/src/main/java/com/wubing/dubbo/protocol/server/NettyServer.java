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
public class NettyServer {
    private static final Log logger = LogFactory.getLog(NettyServer.class);
    private static final ClassLoader CLASS_LOADER = NettyServer.class.getClassLoader();
    private ChannelFuture channelFuture;
    private NioEventLoopGroup boss;

    public NettyServer(String host, int port) {
        logger.info("NettyServer init ...");

        this.init(host, port);
        this.start();
    }

    private void init(String host, int port) {
        boss = new NioEventLoopGroup();
        channelFuture = new ServerBootstrap()
                .group(boss)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(CLASS_LOADER)));
                        ch.pipeline().addLast(new ObjectEncoder());
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                }).bind(new InetSocketAddress(host, port));
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Channel channel = channelFuture.sync().channel();
                    channel.closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (boss != null) {
                        boss.shutdownGracefully();
                    }
                }
            }
        }).start();
    }

}
