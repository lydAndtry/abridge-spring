package cn.abridge.springframework.beans;

import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/3/20 21:43
 * @Description: bean包和子包中抛出的所有异常的抽象超类
 * <p>
 *     在源码中，这里是继承了抽象类NestedRuntimeException，这里为了方便就只是继承RuntimeException
 * </p>
 */
public class BeansException extends RuntimeException {
    /**
     * 用指定的消息创建一个新的BeansException
     * @param msg 详细信息
     */
    public BeansException(String msg) {
        super(msg);
    }

    /**
     * 使用指定的消息和根本原因创建新的BeansException。
     * @param msg 详细信息
     * @param cause 根本原因
     */
    public BeansException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
