package cn.abridge.springframework.test;

import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import cn.abridge.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.abridge.springframework.test.bean.UserService;
import org.junit.Test;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:54
 * @Description: 测试类
 */
public class ApiTest {

    @Test
    public void test_BeanFactory(){
        String beanName = "userService";
        // 1、初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2、注册bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        // 3、获取bean
        UserService bean = (UserService) beanFactory.getBean(beanName);
        bean.getUserInfo();
        System.out.println("第一次的bean对象：" + bean);
        // 4、从单例池获取bean
        UserService singletonBean = (UserService) beanFactory.getBean(beanName);
        singletonBean.getUserInfo();
        System.out.println("第二次的bean对象：" + singletonBean);
    }
}
