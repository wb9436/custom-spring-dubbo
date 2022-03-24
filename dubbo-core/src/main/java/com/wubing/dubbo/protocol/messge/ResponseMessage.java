package com.wubing.dubbo.protocol.messge;

import java.io.Serializable;

/**
 * 响应消息
 *
 * @author: WB
 * @version: v1.0
 */
public class ResponseMessage implements Serializable {
    private final long messageId;
    private Throwable failure;
    private Object result;

    public ResponseMessage(long messageId) {
        this.messageId = messageId;
    }

    public long getMessageId() {
        return messageId;
    }

    public Throwable getFailure() {
        return failure;
    }

    public void setFailure(Throwable failure) {
        this.failure = failure;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return failure == null;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "messageId=" + messageId +
                ", failure=" + failure +
                ", result=" + result +
                '}';
    }
}
