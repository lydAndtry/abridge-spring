package cn.abridge.springframework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:45
 * @Description: bean工厂
 */
public class BeanFactory {

    /** bean定义对象的集合，按bean名称键。 */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /** 获取bean */
    public Object getBean(String name) {
        return beanDefinitionMap.get(name).getBean();
    }

    /** 注册bean到bean对象集合 */
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }
}
