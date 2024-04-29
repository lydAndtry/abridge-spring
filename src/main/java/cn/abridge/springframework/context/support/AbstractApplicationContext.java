package cn.abridge.springframework.context.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.abridge.springframework.beans.factory.config.BeanPostProcessor;
import cn.abridge.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import cn.abridge.springframework.context.ConfigurableApplicationContext;
import cn.abridge.springframework.core.io.DefaultResourceLoader;

import java.util.Map;

/**
 * @Author: lyd
 * @Date: 2024/4/18 21:41
 * @Description: 这是 {@link cn.abridge.springframework.context.ApplicationContext} 的抽象实现。
 * 它并没有强制规定用于存储配置的类型，只是实现了常见的上下文功能。
 * 使用了模板方法设计模式，需要具体的子类来实现抽象方法。
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader
        implements ConfigurableApplicationContext {

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        // 实现刷新容器
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        // 注册Bean实例化前的处理器
        invokeBeanFactoryPostProcessors(beanFactory);
        // 注册Bean前后置处理器
        registerBeanPostProcessors(beanFactory);
        // 实例化所有剩余的（非延迟初始化）单例。
        finishBeanFactoryInitialization(beanFactory);
    }

    /**
     * 实例化并调用所有已注册的BeanFactoryPostProcessor beans，
     * 如果给定，将遵守明确的顺序。
     * <p>必须在单例实例化之前调用。
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    /**
     * 完成此上下文的bean工厂的初始化，初始化所有剩余的单例bean。
     * @param beanFactory bean工厂
     */
    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        // 目前只做一件事：提前实例化单例bean
        beanFactory.preInstantiateSingletons();
    }

    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }

    /**
     * 子类必须在这里返回他们的内部bean工厂。他们应该有效地实现查找，这样可以在不影响性能的情况下反复调用。
     * @return 这个应用上下文的内部bean工厂（永远不会是{@code null}）
     * @throws IllegalStateException /
     */
    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;


    /**
     * 子类必须实现这个方法来执行实际的配置加载。此方法由 {@link #refresh()} 在进行任何其他初始化工作之前调用。
     * <p>一个子类将要么创建一个新的bean工厂并持有其引用，要么返回它持有的单个BeanFactory实例。
     * 在后一种情况下，如果多次刷新上下文，它通常会抛出IllegalStateException。</p>
     * @throws BeansException /
     */
    protected abstract void refreshBeanFactory() throws BeansException;
}
