package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lyd
 * @Date: 2024/3/20 21:15
 * @Description: 默认单例bean对象的注册器
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /** 单例bean注册的集合（存放单例bean对象） */
    private final Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    /**
     * 注册bean
     * @param beanName bean名
     * @param singletonObject 单例bean对象
     */
    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }
}
