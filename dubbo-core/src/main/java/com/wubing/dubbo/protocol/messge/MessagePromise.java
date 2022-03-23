package com.wubing.dubbo.protocol.messge;

/**
 * 异步结果
 *
 * @author: WB
 * @version: v1.0
 */
public class MessagePromise {
    private final int messageId;
    private volatile int resultType;
    private Object result;
    private Exception exception;

    public MessagePromise(int messageId) {
        this.messageId = messageId;
        this.resultType = ResultType.WAITING.ordinal();
        this.result = null;
    }

    public void waiting() {
        int timeout = 10000; //3s超时
        long startTime = System.currentTimeMillis();
        //阻塞等待
        while (resultType == ResultType.WAITING.ordinal()) {
            if (System.currentTimeMillis() > (startTime + timeout)) {
                break;
            }
            //阻塞等待
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }

    public int getMessageId() {
        return messageId;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType.ordinal();
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

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isSuccess() {
        return resultType == ResultType.SUCCESS.ordinal();
    }

    public boolean isFailure() {
        return resultType == ResultType.FAILURE.ordinal();
    }

    public boolean isTimeout() {
        return resultType == ResultType.WAITING.ordinal();
    }
}
