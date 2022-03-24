package com.wubing.dubbo.protocol.server;

import com.wubing.dubbo.support.proxy.ServiceProxyFactory;
import com.wubing.dubbo.protocol.messge.RequestMessage;
import com.wubing.dubbo.protocol.messge.ResponseMessage;
import com.wubing.dubbo.protocol.messge.ResultType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * Netty客户端处理类
 *
 * @author: WB
 * @version: v1.0
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Log logger = LogFactory.getLog(NettyServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("收到客户端请求：" + msg);
        RequestMessage message = (RequestMessage) msg;

        String className = message.getClassName();
        ResponseMessage respMessage = new ResponseMessage(message.getMessageId());
        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(message.getMethodName(), message.getParameterTypes());
            Object instance = ServiceProxyFactory.getBean(clazz);
            Object result = method.invoke(instance, message.getParameterValues());

            respMessage.setResultType(ResultType.SUCCESS.ordinal());
            respMessage.setResult(result);
        } catch (Exception e) {
            e.printStackTrace();

            respMessage.setResultType(ResultType.FAILURE.ordinal());
            respMessage.setResult(e);
        }
        ctx.writeAndFlush(respMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("客户端断开连接。。。");
    }
}
