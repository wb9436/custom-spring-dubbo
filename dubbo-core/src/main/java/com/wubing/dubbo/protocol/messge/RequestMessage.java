package com.wubing.dubbo.protocol.messge;

import com.wubing.dubbo.util.IncreaseIdUtil;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 消息体
 *
 * @author: WB
 * @version: v1.0
 */
public class RequestMessage implements Serializable {
    private final long messageId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameterValues;

    public RequestMessage(String className, String methodName, Class<?>[] parameterTypes, Object[] parameterValues) {
        this.messageId = IncreaseIdUtil.getId();
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameterValues = parameterValues;
    }

    public long getMessageId() {
        return messageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    @Override
    public String toString() {
        return "RequestMessage{" +
                "messageId=" + messageId +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameterValues=" + Arrays.toString(parameterValues) +
                '}';
    }
}
