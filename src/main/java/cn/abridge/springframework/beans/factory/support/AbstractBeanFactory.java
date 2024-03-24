package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.BeanFactory;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;

/**
 * @Author: lyd
 * @Date: 2024/3/20 21:33
 * @Description: 抽象类定义模板方法
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    /**
     * 实现BeanFactory获取bean对象
     * @param name bean名
     * @return bean对象
     */
    @Override
    public Object getBean(String name) {
        // 获取单例bean对象
        Object bean = getSingleton(name);
        if (bean != null) {
            return bean;
        }
        // 创建bean定义
        BeanDefinition beanDefinition = getBeanDefinition(name);
        // 调用创建bean对象
        return createBean(name, beanDefinition);
    }

    /**
     * 返回给定bean名称的bean定义
     * <p>
     *     这里是根据Spring源码中保持一样，在源码中是个抽象方法，由具体的DefaultListableBeanFactory类进行实现
     * </p>
     * @param beanName bean名称
     * @return bean定义
     * @throws BeansException 抛出的bean异常
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 获取bean对象
     * <p>
     *     这里与spring源码不同的一点是，这里少了显式参数
     * </p>
     * @param beanName bean名称
     * @param mbd bean定义
     * @return bean对象
     * @throws BeansException 抛出的bean异常
     */
    protected abstract Object createBean(String beanName, BeanDefinition mbd) throws BeansException;
}
