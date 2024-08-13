package cn.abridge.springframework.test.bean;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.BeanFactory;
import cn.abridge.springframework.beans.factory.BeanFactoryAware;
import cn.abridge.springframework.context.ApplicationContext;
import cn.abridge.springframework.context.ApplicationContextAware;

/**
 * @Author: lyd
 * @Date: 2024/8/13 21:35
 * @Description:
 */
public class AwareTestBean implements ApplicationContextAware, BeanFactoryAware {
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
