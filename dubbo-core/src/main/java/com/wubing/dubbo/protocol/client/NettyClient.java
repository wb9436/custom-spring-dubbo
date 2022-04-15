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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Netty客户端
 *
 * @author: WB
 * @version: v1.0
 */
@Slf4j
public class NettyClient implements Runnable {
    private static final Log logger = LogFactory.getLog(NettyClient.class);
    private static final ClassLoader CLASS_LOADER = NettyClient.class.getClassLoader();
    /**
     * 最大尝试次数：12
     */
    private static final int MAX_RECONNECT_TIMES = 12;
    /**
     * 每次尝试间隔：5s
     */
    private static final int RECONNECT_INTERVAL = 5;

    private final String host;
    private final Integer port;
    private static Channel channel;
    private NioEventLoopGroup worker;
    private int reconnectTimes; //重连次数

    public NettyClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    private void connect() throws Exception {
        worker = new NioEventLoopGroup();
        NettyClient that = this;
        ChannelFuture channelFuture = new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(CLASS_LOADER)));
                        ch.pipeline().addLast(new ObjectEncoder());
                        ch.pipeline().addLast(new NettyClientHandler(that));
                    }
                }).connect(new InetSocketAddress(host, port));
        channel = channelFuture.sync().channel();
        reconnectTimes = 0;
        log.info("Netty Client 连接成功：{}:{}", host, port);
        channel.closeFuture().sync();
    }

    /**
     * 连接/重连
     */
    protected void reconnect() {
        try {
            connect();
        } catch (Exception e) {
            if (worker != null) {
                worker.shutdownGracefully();
            }
            log.error("Netty Client 连接失败：{}", e.getMessage());
            // 间隔 5s 重连一次
            try {
                TimeUnit.SECONDS.sleep(RECONNECT_INTERVAL);
            } catch (InterruptedException ex) {
            }
            reconnectTimes++;
            if (reconnectTimes > MAX_RECONNECT_TIMES) {
                return;
            }
            log.info("Netty Client 正在重试连接：当前第【{}】次重试", reconnectTimes);
            reconnect();
        }
    }

    @Override
    public void run() {
        reconnect();
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
