package cn.abridge.springframework.beans.factory.config;

import cn.abridge.springframework.beans.BeansException;
import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/4/18 21:22
 * @Description: 工厂钩子，允许对新的bean实例进行自定义修改 &mdash; 例如，检查标记接口或用代理包装beans。
 */
public interface BeanPostProcessor {

    /**
     * 在进行任何bean初始化回调（如InitializingBean的 {@code afterPropertiesSet} 或自定义init-method）<i>之前</i>，
     * 将此 {@code BeanPostProcessor} 应用于给定的新bean实例。实例已经被填充了属性值。返回的bean实例可能是原始实例的包装。
     * <p>默认的实现方法是原样返回给定的 {@code bean}。
     * @param bean 新的bean实例
     * @param beanName bean的名称
     * @return 要使用的bean实例，可以是原始的或者是封装后的；
     * 如果是{@code null}，则不会调用后续的 BeanPostProcessors。
     * @throws BeansException 出现错误的时候
     */
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 在进行任何bean初始化回调（例如InitializingBean的{@code afterPropertiesSet}或自定义的init-method）后，
     * 对给定的新bean实例<i>应用<i>此{@code BeanPostProcessor}。
     * 此时，bean已经被赋予了属性值。返回的bean实例可能是围绕原始bean的包装器。
     * <p>在是FactoryBean的情况下，此回调将同时被调用于FactoryBean实例和由FactoryBean创建的对象（自Spring 2.0开始）。
     * 后处理器可以通过相应的 {@code bean instanceof FactoryBean}
     * 检查来决定是否同时应用于FactoryBean和创建的对象，或仅应用于其中之一。
     * @param bean 新的bean实例
     * @param beanName bean的名称
     * @return 要使用的bean实例，可以是原始的或者是封装后的；
     * @throws BeansException 出现错误的时候
     */
    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
