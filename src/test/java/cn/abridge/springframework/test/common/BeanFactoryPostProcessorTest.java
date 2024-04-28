package cn.abridge.springframework.test.common;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.PropertyValue;
import cn.abridge.springframework.beans.PropertyValues;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import cn.abridge.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.abridge.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @Author: lyd
 * @Date: 2024/4/20 17:02
 * @Description:
 */
public class BeanFactoryPostProcessorTest implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println(this.getClass().getName() + "---未实例化前的处理>>> ");
        BeanDefinition userServiceBeanDefinition = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = userServiceBeanDefinition.getPropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("tel", "86-0595"));
    }
}
