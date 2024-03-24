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
}
