package com.wubing.dubbo.protocol.messge;

import java.io.Serializable;

/**
 * 响应消息
 *
 * @author: WB
 * @version: v1.0
 */
public class ResponseMessage implements Serializable {
    private final int messageId;
    private int resultType;
    private Object result;

    public ResponseMessage(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return resultType == ResultType.SUCCESS.ordinal();
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "messageId=" + messageId +
                ", resultType=" + resultType +
                ", result=" + result +
                '}';
    }
}
