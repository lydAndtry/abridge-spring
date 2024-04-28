package cn.abridge.springframework.test.common;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanPostProcessor;
import cn.abridge.springframework.test.bean.UserService;

/**
 * @Author: lyd
 * @Date: 2024/4/20 16:54
 * @Description:
 */
public class BeanPostProcessorTest implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(this.getClass().getName() + "---初始化前置处理>>> beanName: " + beanName);
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setAddress("福建泉州");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(this.getClass().getName() + "---初始化后置处理>>> beanName: " + beanName);
        return bean;
    }
}
