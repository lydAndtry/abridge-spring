package cn.abridge.springframework.beans.factory.config;

import cn.abridge.springframework.beans.factory.HierarchicalBeanFactory;

/**
 * @Author: lyd
 * @Date: 2024/4/6 14:36
 * @Description: 可配置Bean工厂接口
 * <p>这是一个大多数bean工厂需要实现的配置接口。
 * 除了在 {@link cn.abridge.springframework.beans.factory.BeanFactory}
 * 接口中的bean工厂客户端方法外，它还提供了配置bean工厂的功能。
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
    /**
     * 添加一个新的BeanPostProcessor，它将应用于由此工厂创建的beans。该操作应在工厂配置期间被调用。
     * @param beanPostProcessor /
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
