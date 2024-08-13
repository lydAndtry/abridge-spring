package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.PropertyValue;
import cn.abridge.springframework.beans.PropertyValues;
import cn.abridge.springframework.beans.factory.*;
import cn.abridge.springframework.beans.factory.config.AutowireCapableBeanFactory;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import cn.abridge.springframework.beans.factory.config.BeanPostProcessor;
import cn.abridge.springframework.beans.factory.config.BeanReference;
import cn.abridge.springframework.core.NativeDetector;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @Author: lyd
 * @Date: 2024/3/20 22:10
 * @Description: 实例化Bean类 自动装配能力Bean工厂, 在spring源码中还会去实现AutowireCapableBeanFactory，这里就简单操作
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
        implements AutowireCapableBeanFactory {

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
            this.instantiationStrategy = new CglibSubclassingInstantiationStrategy();
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
            // 给 Bean 填充属性
            applyPropertyValues(beanName, bean, mbd);
            // 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            initializeBean(beanName, bean, mbd);
        } catch (Exception e) {
            throw new BeansException("bean对象的初始化失败!", e);
        }
        //注册有销毁方法的bean
        registerDisposableBeanIfNecessary(beanName, bean, mbd);
        // 添加单例bean
        addSingleton(beanName, bean);
        return bean;
    }

    /**
     * 将给定的Bean添加到工厂中可销毁Bean的列表中，注册其DisposableBean接口和/或指定的销毁方法，
     * 以便在工厂关闭时调用（如果适用）。此操作仅适用于单例模式的Bean。
     * @param beanName bean名
     * @param bean bean实例
     * @param mbd bean定义
     */
    private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition mbd) {
        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(mbd.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, mbd));
        }
    }

    /**
     * 初始化给定的Bean实例，应用工厂回调、初始化方法以及Bean后置处理器。
     * <p>从 {@link #createBean} 调用，用于传统定义的Bean；从 {@link #initializeBean} 调用，用于已有的Bean实例。</p>
     * @param beanName 工厂中的bean名称
     * @param bean Bean对象
     * @param mbd Bean定义
     * @return 初始化后的Bean实例（可能被包装过）。
     */
    private Object initializeBean(String beanName, Object bean, BeanDefinition mbd) {
        invokeAwareMethods(beanName, bean);
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        // 在此处执行bean的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, mbd);
        } catch (Throwable ex) {
            throw new BeansException("调用bean [" + beanName + "] 初始化方法失败", ex);
        }
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }

    /**
     * 回调感知方法
     * @param beanName bean名称
     * @param bean bean
     */
    private void invokeAwareMethods(String beanName, Object bean) {
        if (bean instanceof Aware) {
            // 其他感知的设置
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
            }
        }
    }

    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition mbd) throws Exception {
        // 这里简化操作，判断是否InitializingBean
        boolean isInitializingBean = (bean instanceof InitializingBean);
        if (isInitializingBean) {
            // 强转并执行
            ((InitializingBean) bean).afterPropertiesSet();
        }
        String initMethodName = mbd.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName) &&
                !(isInitializingBean && "afterPropertiesSet".equals(initMethodName))) {
            // 调用自定义初始化方法
            invokeCustomInitMethod(beanName, bean, mbd);
        }
    }

    private void invokeCustomInitMethod(String beanName, Object bean, BeanDefinition mbd) throws Exception {
        // 通过反射完成
        String initMethodName = mbd.getInitMethodName();
        Assert.state(initMethodName != null, "没有定义的初始化方法");
        Method initMethod = ClassUtil.getPublicMethod(mbd.getBeanClass(), initMethodName);
        if (initMethod == null) {
            throw new BeansException("无法在名为 '" + beanName + "' 的bean上找到名为 '" + initMethodName + "' 的初始化方法");
        }
        initMethod.invoke(bean);
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        // 结果先赋值原先的bean实例
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        // 结果先赋值原先的bean实例
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    private void applyPropertyValues(String beanName, Object bean, BeanDefinition mbd) {
        try {
            // 通过bean定义获取属性对象的容器
            PropertyValues propertyValues = mbd.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                // 这里通过判断是否为BeanReference类，是的话表示是个依赖的Bean对象，需要通过获取Bean，
                // 不存在的话会通过创建这个bean对象（创建的时候需要bean定义，因为早之前就已经注册，
                // 在内存中是可以获取到的）。
                if (value instanceof BeanReference) {
                    // A依赖B，实例化B
                    BeanReference reference = (BeanReference) value;
                    value = getBean(reference.getBeanName());
                }
                // 通过反射来实现填充属性 （BeanUtil: Hutool工具包下的方法）
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Bean " + beanName + " 设置属性值错误");
        }
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
