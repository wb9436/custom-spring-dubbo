package com.wubing.dubbo.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 获取自增ID（保证唯一）
 *
 * @author: WB
 * @version: v1.0
 */
public class IncreaseIdUtil {
    private static final AtomicLong MESSAGE_ID_CREATOR = new AtomicLong();

    /**
     * 获取自增ID
     */
    public static long getId() {
        return MESSAGE_ID_CREATOR.incrementAndGet();
    }

}
