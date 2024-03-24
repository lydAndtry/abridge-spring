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
}
