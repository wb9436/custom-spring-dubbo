package com.wubing.dubbo.protocol.client;

import com.wubing.dubbo.protocol.messge.MessagePromise;
import com.wubing.dubbo.protocol.messge.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Netty客户端处理类
 *
 * @author: WB
 * @version: v1.0
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ResponseMessage> {
    private static final Log logger = LogFactory.getLog(NettyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage message) {
        logger.info("收到服务端响应：" + message);
        long messageId = message.getMessageId();
        MessagePromise promise = MessagePromise.getPromise(messageId);
        if (promise != null) {
            if (message.isSuccess()) {
                promise.setResult(message.getResult());
            } else {
                promise.setFailure(message.getFailure());
            }
        }
    }
}
