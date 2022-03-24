package com.wubing.dubbo.protocol.messge;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * 消息应答
 *
 * @author: WB
 * @version: v1.0
 */
public class MessagePromise {
    private static final Map<Long, MessagePromise> MESSAGE_QUEUE = new ConcurrentHashMap<>();

    private static final long DEFAULT_TIMEOUT = 6000;
    private static final int WAITING = 0;
    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;

    //消息状态：0-等待；1=成功；2=失败
    private volatile int state;
    //消息ID
    private final long msgId;
    //错误结果
    private Throwable failure;
    //正确结果
    private Object result;

    public MessagePromise(long msgId) {
        this.state = WAITING;
        this.msgId = msgId;
        this.failure = null;
        this.result = null;
        MESSAGE_QUEUE.put(this.msgId, this);
    }

    public void setFailure(Throwable failure) {
        this.failure = failure;
        this.state = FAILURE;
    }

    public void setResult(Object result) {
        this.result = result;
        this.state = SUCCESS;
    }

    public static MessagePromise getPromise(long msgId) {
        return MESSAGE_QUEUE.get(msgId);
    }

    public boolean isSuccess() {
        return this.state == SUCCESS;
    }

    public Throwable getFailure() {
        MESSAGE_QUEUE.remove(this.msgId);
        return failure;
    }

    public Object getResult() {
        MESSAGE_QUEUE.remove(this.msgId);
        return result;
    }

    /**
     * 等待响应
     */
    public void waiting() {
        this.waiting(DEFAULT_TIMEOUT);
    }

    /**
     * 阻塞等待响应
     */
    public void waiting(long timeout) {
        long startTime = System.currentTimeMillis();
        while (true) {
            if (state != WAITING) {
                break;
            }
            if (System.currentTimeMillis() > (startTime + timeout)) {
                if (state == WAITING) {
                    this.state = FAILURE;
                    this.failure = new TimeoutException("waiting response timeout");
                }
                break;
            }
            //休眠等待，避免CPU占用过高
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

}