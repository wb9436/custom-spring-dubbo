package com.wubing.test;

import com.wubing.dubbo.protocol.messge.RequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

/**
 * @author: WB
 * @version: v1.0
 */
public class NettyClientTest {
    private static final ClassLoader CLASS_LOADER = NettyClientTest.class.getClassLoader();

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(CLASS_LOADER)));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast("handler", new SimpleChannelInboundHandler() {

                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("收到客户端请求：" + msg);
                                }
                            });
                        }
                    }).connect(new InetSocketAddress("127.0.0.1", 20880));
            Channel channel = channelFuture.sync().channel();
            //发送数据
            RequestMessage message = new RequestMessage("com.wubing.service.HelloService", "sayHello", new Class[]{String.class}, new String[]{"张三"});
            System.out.println(message);
            channel.writeAndFlush(message);
//            //异步断开连接，避免阻塞主线程
//            channel.closeFuture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
