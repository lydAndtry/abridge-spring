package cn.abridge.springframework.beans.factory.config;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.ListableBeanFactory;

/**
 * @Author: lyd
 * @Date: 2024/4/2 23:05
 * @Description: 可配置监听Bean工厂
 * <p>这是一个大多数可列出bean工厂需要实现的配置接口。除了 {@link ConfigurableBeanFactory} 外，
 * 它还提供了分析和修改bean定义，以及预先实例化单例的功能。</p>
 */
public interface ConfigurableListableBeanFactory
        extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    /**
     * 返回指定bean的已注册BeanDefinition，允许访问其属性值和构造函数参数值
     * （这些值可以在bean工厂后处理期间被修改）。
     * @param beanName bean名称
     * @return 注册的BeanDefinition
     * @throws BeansException 如果在此工厂中没有定义具有给定名称的bean
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 抢先实例化bean，
     * 确保所有非懒加载的单例都被实例化
     * @throws BeansException 创建bean发生异常
     */
    void preInstantiateSingletons() throws BeansException;
}
