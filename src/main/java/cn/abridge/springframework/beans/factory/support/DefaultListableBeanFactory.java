package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: lyd
 * @Date: 2024/3/20 22:27
 * @Description: 默认的Bean工厂 （核心类）
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    /** bean定义对象的映射，按bean名称键 */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeansException {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("bean 名称为 '" + beanName + "' 未定义");
        }
        return beanDefinition;
    }
}
