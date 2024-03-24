package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;

/**
 * @Author: lyd
 * @Date: 2024/3/20 22:10
 * @Description: 实例化Bean类 自动装配能力Bean工厂, 在spring源码中还会去实现AutowireCapableBeanFactory，这里就简单操作
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    /**
     * 创建bean对象（生产bean实例）
     * @param beanName bean名称
     * @param mbd bean定义
     * @return bean实例
     * @throws BeansException bean异常
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition mbd) throws BeansException {
        Object bean = null;
        try {
            // 创建bean实例
            bean = mbd.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeansException("bean对象的初始化失败!");
        }
        // 添加单例bean
        addSingleton(beanName, bean);
        return bean;
    }
}
