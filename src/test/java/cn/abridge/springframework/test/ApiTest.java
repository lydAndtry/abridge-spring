package cn.abridge.springframework.test;

import cn.abridge.springframework.beans.PropertyValue;
import cn.abridge.springframework.beans.PropertyValues;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import cn.abridge.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.abridge.springframework.beans.factory.config.BeanReference;
import cn.abridge.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.abridge.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.abridge.springframework.context.ApplicationContext;
import cn.abridge.springframework.context.support.ClassPathXmlApplicationContext;
import cn.abridge.springframework.core.io.DefaultResourceLoader;
import cn.abridge.springframework.core.io.Resource;
import cn.abridge.springframework.test.bean.AwareTestBean;
import cn.abridge.springframework.test.bean.UserDao;
import cn.abridge.springframework.test.bean.UserService;
import cn.abridge.springframework.test.common.BeanFactoryPostProcessorTest;
import cn.abridge.springframework.test.common.BeanPostProcessorTest;
import cn.hutool.core.io.IoUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:54
 * @Description: 测试类
 */
public class ApiTest {

    @Test
    public void test_awareUse2() throws IOException {
        String beanName = "userService";
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Object bean = applicationContext.getBean(beanName);
    }

    @Test
    public void test_awareUse1() throws IOException {
        String beanName = "awareTestBean";
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        AwareTestBean bean = applicationContext.getBean(beanName, AwareTestBean.class);
        ApplicationContext app = bean.getApplicationContext();
        System.out.println("aware: " + app);
        System.out.println("applicationContext: " + applicationContext);
        System.out.println(bean.getBeanFactory());
    }

    // 初始化测试方法
    @Test
    public void test_InitBeanAndDestroyMethod() throws IOException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();
    }

    @Test
    public void test_ApplicationContext() throws IOException {
        // 1. 获取上下文应用
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        // 2. 获取Bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.getUserInfo();
    }

    @Test
    public void test_BeanPostProcessor() {
        // 1 获取默认的Bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2 解析XML, 注册Bean
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        // 3.1 执行Bean实例化前置处理
        BeanFactoryPostProcessorTest beanFactoryPostProcessorTest = new BeanFactoryPostProcessorTest();
        beanFactoryPostProcessorTest.postProcessBeanFactory(beanFactory);
        // 3.2 实例化之后的处理
        BeanPostProcessorTest beanPostProcessorTest = new BeanPostProcessorTest();
        beanFactory.addBeanPostProcessor(beanPostProcessorTest);
        // 4 获取bean对象
        UserService userService = beanFactory.getBean("userService", UserService.class);
        userService.getUserInfo();
    }

    @Test
    public void test_ReadXmlFile() {
        String beanName = "userService";
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 获取xml文件的bean读取器，这里会实例化好bean定义注册器和资源加载器
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        // 加载xml文件中的资源
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        // 3、获取bean
        UserService bean = (UserService) beanFactory.getBean(beanName, UserService.class);
        bean.getUserInfo();
        System.out.println("UserService的bean对象：" + bean);
        System.out.println("UserService的bean的dao对象：" + bean.getUserDao());
    }


    @Test
    public void test_Resource() throws IOException {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        System.out.println("======测试获取classpath:下的资源======");
        // 测试获取classpath:下的资源
        Resource resource = resourceLoader.getResource("classpath:lyd.txt");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        System.out.println("======测试使用文件系统资源======");
        // 测试使用文件系统资源
        resource = resourceLoader.getResource("src/test/resources/lyd.txt");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        System.out.println("======测试使用远程文件======");
        // 测试使用远程文件
        resource = resourceLoader.getResource("https://github.com/");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }


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
