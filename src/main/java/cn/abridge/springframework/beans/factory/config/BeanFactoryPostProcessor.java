package cn.abridge.springframework.beans.factory.config;

import cn.abridge.springframework.beans.BeansException;

/**
 * @Author: lyd
 * @Date: 2024/4/18 21:16
 * @Description: 工厂钩子，允许自定义修改应用上下文的bean定义，适应上下文底层bean工厂的bean属性值。
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在应用上下文的内部bean工厂进行标准初始化后进行修改。
     * 所有的bean定义都将被加载，但是还没有实例化任何bean。
     * 这允许对即使是对急切初始化的bean进行属性的覆盖或添加。
     * @param beanFactory 应用上下文使用的bean工厂
     * @throws BeansException 在出现错误的情况下。
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
