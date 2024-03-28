package cn.abridge.springframework.beans.factory;

import cn.abridge.springframework.beans.BeansException;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:45
 * @Description: bean工厂
 */
public interface BeanFactory {

    /**
     * 获取bean
     * @param name bean名
     * @return bean对象
     * @throws BeansException 如果拿不到bean
     */
    Object getBean(String name) throws BeansException;

    /**
     * 返回指定bean的一个实例，它可以是共享的，也可以是独立的
     * @param name bean的名称
     * @param args 使用显式参数创建bean实例时使用的参数
     * @return bean的实例
     * @throws BeansException 如果创建失败抛出的异常
     */
    Object getBean(String name, Object... args) throws BeansException;
}
