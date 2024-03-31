package cn.abridge.springframework.util;

import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/3/28 21:49
 * @Description: 断言类
 */
public abstract class Assert {
    /**
     * 断言一个对象不是 {@code null}
     * @param object 检测的对象
     * @param message 为空时候的提示信息
     * @throws IllegalArgumentException 如果对象是{@code null}
     */
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
