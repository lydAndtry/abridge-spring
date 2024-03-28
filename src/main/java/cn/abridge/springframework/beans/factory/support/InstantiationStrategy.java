package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Constructor;

/**
 * @Author: lyd
 * @Date: 2024/3/25 21:04
 * @Description: 负责创建与根bean定义相对应实例的接口
 * <p>由于有各种可能的方法，包括使用CGLIB动态地创建子类以支持方法注入，因此可以将此提取到策略中。</p>
 */
public interface InstantiationStrategy {

    /**
     *返回具有给定名称的bean实例，并通过给定构造函数创建它。
     * @param bd bean定义
     * @param beanName bean名称
     * @param ctor 构造函数
     * @param args 构造函数参数
     * @return bean对象
     * @throws BeansException 如果实例化失败，抛出异常
     */
    Object instantiate(BeanDefinition bd, @Nullable String beanName, Constructor ctor,
                       Object... args) throws BeansException;
}
