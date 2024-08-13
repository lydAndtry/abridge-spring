package cn.abridge.springframework.context;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.Aware;

/**
 * @Author: lyd
 * @Date: 2024/8/13 21:18
 * @Description: 接口可以被任何对象实现，能够感知{@link ApplicationContext}
 */
public interface ApplicationContextAware extends Aware {
    /**
     * 设置该对象运行的ApplicationContext。通常，这个调用将用于初始化对象
     * @param applicationContext 该对象要使用的ApplicationContext对象
     * @throws BeansException 异常
     */
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
