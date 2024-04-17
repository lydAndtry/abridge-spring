package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;

/**
 * @Author: lyd
 * @Date: 2024/3/20 22:29
 * @Description: bean定义注册器
 */
public interface BeanDefinitionRegistry {

    /**
     * bean注册器接口
     * @param beanName bean名称
     * @param beanDefinition bean定义
     * @throws BeansException 统一异常，在spring源码中是BeanDefinitionStoreException
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeansException;

    /**
     * 根据bean名称获取bean定义
     * @param beanName bean名称
     * @return bean定义 (找不到的话 {@code null})
     * @throws BeansException 如果找不到对应的bean定义
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 检查此注册中心是否包含具有给定名称的bean定义。
     * @param beanName 要查找的bean名
     * @return 如果此注册中心包含具有给定名称的bean定义
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 返回此注册中心中定义的所有bean的名称
     * @return bean名称数组或者空的数组
     */
    String[] getBeanDefinitionNames();
}
