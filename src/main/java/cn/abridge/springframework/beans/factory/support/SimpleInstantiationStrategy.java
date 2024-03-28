package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author: lyd
 * @Date: 2024/3/25 21:15
 * @Description: 简单实例化策略
 * <p>用来实例化bean对象</p>
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition bd, String beanName, Constructor ctor, Object... args) throws BeansException {
        Class clazz = bd.getBeanClass();
        try {
            if (null != ctor) {
                // 根据参数类型来获取类的构造函数，并实例化
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            } else {
                // 实例化无参对象
                return clazz.newInstance();
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeansException("实例化 [" + clazz.getName() + "] 失败！", e);
        }
    }
}
