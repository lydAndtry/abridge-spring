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
    public void test_BeanFactoryUseCGLIB(){
        String beanName = "userService";
        // 1、初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2、注册bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        // 3、获取bean
        UserService bean = (UserService) beanFactory.getBean(beanName, "liyongde", "25");
        bean.getUserInfo();
        System.out.println("UserService的bean对象：" + bean);
    }

    @Test
    public void test_GraalVM() {
        System.out.println(System.getProperty("org.graalvm.nativeimage.imagecode"));
    }
}
