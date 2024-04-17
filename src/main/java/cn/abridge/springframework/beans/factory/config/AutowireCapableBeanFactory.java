package cn.abridge.springframework.beans.factory.config;

import cn.abridge.springframework.beans.factory.BeanFactory;

/**
 * @Author: lyd
 * @Date: 2024/4/6 14:47
 * @Description: 这是 {@link BeanFactory} 接口的扩展，
 * 适用于能够进行自动装配的bean工厂，只要他们希望将此功能暴露给现有的bean实例。
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
}
