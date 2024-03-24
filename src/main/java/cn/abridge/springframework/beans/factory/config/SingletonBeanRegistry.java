package cn.abridge.springframework.beans.factory.config;

/**
 * @Author: lyd
 * @Date: 2024/3/20 21:12
 * @Description: 单例注册接口
 */
public interface SingletonBeanRegistry {
    /**
     * 获取单例bean对象
     * 返回以给定名称注册的(原始)单例对象。
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);
}
