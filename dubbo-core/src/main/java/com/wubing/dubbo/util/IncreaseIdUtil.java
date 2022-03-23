package com.wubing.dubbo.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 获取自增ID（保证唯一）
 *
 * @author: WB
 * @version: v1.0
 */
public class IncreaseIdUtil {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 获取自增ID
     */
    public static int getId() {
        return ATOMIC_INTEGER.incrementAndGet();
    }

}
