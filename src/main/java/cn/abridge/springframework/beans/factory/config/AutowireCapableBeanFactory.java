package cn.abridge.springframework.beans.factory.config;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.BeanFactory;

/**
 * @Author: lyd
 * @Date: 2024/4/6 14:47
 * @Description: 这是 {@link BeanFactory} 接口的扩展，
 * 适用于能够进行自动装配的bean工厂，只要他们希望将此功能暴露给现有的bean实例。
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 将{@link BeanPostProcessor BeanPostProcessors}应用于给定的现有bean实例，
     * 调用它们的postProcessBeforeInitialization方法。返回的bean实例可能是原始bean的包装器。
     * @param existingBean 现有的bean实例
     * @param beanName bean名称
     * @return 使用的bean实例，可以是原始的，也可以是一个包装过的。
     * @see BeanPostProcessor#postProcessBeforeInitialization(Object, String)
     * @throws BeansException /
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;

    /**
     * 将 {@link BeanPostProcessor BeanPostProcessors} 应用于给定的现有bean实例，
     * 调用它们的postProcessAfterInitialization方法。返回的bean实例可能是原始bean的包装器。
     * @param existingBean 现有的bean实例
     * @param beanName bean名称
     * @return 使用的bean实例，可以是原始的，也可以是一个包装过的。
     * @see BeanPostProcessor#postProcessAfterInitialization
     * @throws BeansException /
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;
}
