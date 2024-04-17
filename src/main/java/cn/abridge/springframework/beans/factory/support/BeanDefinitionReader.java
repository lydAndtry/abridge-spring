package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.core.io.Resource;
import cn.abridge.springframework.core.io.ResourceLoader;
import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/4/1 20:56
 * @Description: 这是一个简单的bean定义读取器接口，它规定了带有 {@link Resource} 和 {@link String} 位置参数的加载方法。
 */
public interface BeanDefinitionReader {

    /**
     * 返回bean工厂来注册bean定义。
     * <p>工厂通过{@link BeanDefinitionRegistry}接口进行显示，封装了与bean定义处理相关的方法。</p>
     * @return bean定义注册器
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * 返回用于bean类的类加载器。
     * <p>{@code null} 建议不要急于加载bean类，而是仅使用类名注册bean定义，相应的类将在以后（或永不）解析
     * @return
     */
    @Nullable
    ResourceLoader getResourceLoader();

    /**
     * 从指定的资源中加载bean定义。
     * @param resource 资源描述符
     * @throws BeansException 在遇到加载或解析错误的情况下
     */
    void loadBeanDefinitions(Resource resource) throws BeansException;

    /**
     * 从指定的资源位置加载bean定义。
     * @param location 资源位置，需要使用 {@code ResourceLoader} 进行加载。
     * （或者这个bean定义读取器的{@code ResourcePatternResolver}）。
     * @throws BeansException 一旦出现加载或解析错误
     */
    void loadBeanDefinitions(String location) throws BeansException;

    /**
     * 从指定的资源位置加载bean定义。
     * @param locations 资源位置，将使用这个bean定义读取器的{@code ResourceLoader}
     *    （或者 {@code ResourcePatternResolver}）进行加载。
     * @throws BeansException 一旦出现加载或解析错误
     */
    void loadBeanDefinitions(String... locations) throws BeansException;
}
