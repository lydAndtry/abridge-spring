package cn.abridge.springframework.beans.factory;

import cn.abridge.springframework.beans.BeansException;
import com.sun.istack.internal.Nullable;

import java.util.Map;

/**
 * @Author: lyd
 * @Date: 2024/4/2 23:07
 * @Description:
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 返回匹配给定对象类型（包括子类）的bean实例，判断来自bean定义或者在FactoryBeans情况下{@code getObjectType} 的值。
     * @param type 要匹配的类或接口，如果匹配所有的具体beans则为{@code null}。
     * @param <T> 类型
     * @return 一个包含匹配的beans的映射表，其中bean的名字作为键，相应的bean实例作为值。
     * @throws BeansException 如果一个bean无法被创建。
     */
    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException;

    /**
     * 返回在此工厂中定义的所有beans的名称。
     * @return 字符串数组
     */
    String[] getBeanDefinitionNames();
}
