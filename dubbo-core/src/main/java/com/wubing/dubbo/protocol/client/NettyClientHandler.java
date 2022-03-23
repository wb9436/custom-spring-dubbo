package com.wubing.dubbo.protocol.client;

import com.wubing.dubbo.protocol.messge.MessageQueue;
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
        int messageId = message.getMessageId();
        MessagePromise promise = MessageQueue.get(messageId);
        if (promise != null) {
            try {
                if (message.isSuccess()) {
                    promise.setResult(message.getResult());
                } else {
                    promise.setException((Exception) message.getResult());
                }
                promise.setResultType(message.getResultType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
