package cn.abridge.springframework.beans.factory;

import cn.abridge.springframework.beans.BeansException;

/**
 * @Author: lyd
 * @Date: 2024/8/13 21:10
 * @Description: 实现该接口是为了能够感知所属于 {@link BeanFactory}
 */
public interface BeanFactoryAware extends Aware {
    /**
     * 向bean实例提供所属工厂的回调。
     * <p>在填充普通的属性的时候调用，但是是在初始化之前，例如：在执行{@link InitializingBean#afterPropertiesSet()}
     * 或者是自定义的init-method方法</p>
     * @param beanFactory bean工厂
     * @throws BeansException 异常
     */
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
