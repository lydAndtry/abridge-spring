package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import cn.abridge.springframework.core.NativeDetector;

import java.lang.reflect.Constructor;

/**
 * @Author: lyd
 * @Date: 2024/3/20 22:10
 * @Description: 实例化Bean类 自动装配能力Bean工厂, 在spring源码中还会去实现AutowireCapableBeanFactory，这里就简单操作
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    /** 创建bean实例的策略。 */
    private InstantiationStrategy instantiationStrategy;

    /**
     * 构造函数
     * 模仿spring源码使用构造器初始化创建bean实例的策略
     */
    public AbstractAutowireCapableBeanFactory() {
        if (NativeDetector.inNativeImage()) {
            this.instantiationStrategy = new SimpleInstantiationStrategy();
        } else {
            this.instantiationStrategy = new SimpleInstantiationStrategy();
        }
    }

    /**
     * 创建bean对象（生产bean实例）
     * @param beanName bean名称
     * @param mbd bean定义
     * @return bean实例
     * @throws BeansException bean异常
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition mbd, Object... args) throws BeansException {
        Object bean;
        try {
            // 创建bean实例 将其抽取出去
            bean = createBeanInstance(mbd, beanName, args);
        } catch (Exception e) {
            throw new BeansException("bean对象的初始化失败!");
        }
        // 添加单例bean
        addSingleton(beanName, bean);
        return bean;
    }

    private Object createBeanInstance(BeanDefinition mbd, String beanName, Object... args) {
        Constructor constructorToUse = null;
        Class<?> beanClass = mbd.getBeanClass();
        // 获取所有构造方法
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor<?> ctor : declaredConstructors) {
            if (null != args && ctor.getParameterTypes().length == args.length) {
                constructorToUse = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(mbd, beanName, constructorToUse, args);
    }

    protected InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }
}
