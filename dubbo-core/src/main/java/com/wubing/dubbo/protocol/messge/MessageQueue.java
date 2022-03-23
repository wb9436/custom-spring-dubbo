package com.wubing.dubbo.protocol.messge;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息队列
 *
 * @author: WB
 * @version: v1.0
 */
public class MessageQueue {
    private static final Map<Integer, MessagePromise> MESSAGE_QUEUE = new ConcurrentHashMap<>();

    public static void put(MessagePromise promise) {
        MESSAGE_QUEUE.put(promise.getMessageId(), promise);
    }

    public static MessagePromise get(int messageId) {
        return MESSAGE_QUEUE.get(messageId);
    }

    public static void clean(MessagePromise promise) {
        MESSAGE_QUEUE.remove(promise.getMessageId());
    }

}
