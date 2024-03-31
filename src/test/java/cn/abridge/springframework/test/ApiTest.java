package cn.abridge.springframework.test;

import cn.abridge.springframework.beans.PropertyValue;
import cn.abridge.springframework.beans.PropertyValues;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import cn.abridge.springframework.beans.factory.config.BeanReference;
import cn.abridge.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.abridge.springframework.test.bean.UserDao;
import cn.abridge.springframework.test.bean.UserService;
import org.junit.Test;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:54
 * @Description: 测试类
 */
public class ApiTest {

    @Test
    public void test_PropertyIntoBeanObject() {
        System.out.println("测试属性填充和依赖注入");
        String beanName = "userService";
        String userDaoBeanName = "userDao";
        // 1、初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2、注册依赖的bean定义
        beanFactory.registerBeanDefinition(userDaoBeanName, new BeanDefinition(UserDao.class));
        // 2.1、属性注入
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("id", "1"));
        propertyValues.addPropertyValue(new PropertyValue(userDaoBeanName, new BeanReference(userDaoBeanName)));
        // 2.2、注册bean定义
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        // 3、获取bean
        UserService bean = (UserService) beanFactory.getBean(beanName);
        bean.getUserInfo();
        System.out.println("UserService的bean对象：" + bean);
        System.out.println("UserService的bean的dao对象：" + bean.getUserDao());
    }

    @Test
    public void test_GraalVM() {
        System.out.println(System.getProperty("org.graalvm.nativeimage.imagecode"));
    }
}
