package cn.abridge.springframework.beans.factory.config;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.ListableBeanFactory;

/**
 * @Author: lyd
 * @Date: 2024/4/2 23:05
 * @Description:
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    /**
     * 返回指定bean的已注册BeanDefinition，允许访问其属性值和构造函数参数值
     * （这些值可以在bean工厂后处理期间被修改）。
     * @param beanName bean名称
     * @return 注册的BeanDefinition
     * @throws BeansException 如果在此工厂中没有定义具有给定名称的bean
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
