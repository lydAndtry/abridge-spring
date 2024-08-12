package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.DisposableBean;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;

import java.lang.reflect.Method;

/**
 * @Author: lyd
 * @Date: 2024/5/10 22:15
 * @Description: 销毁方法适配器
 */
public class DisposableBeanAdapter implements DisposableBean {

    private static final String DESTROY_METHOD_NAME = "destroy";

    private static final String CLOSE_METHOD_NAME = "close";

    private static final String SHUTDOWN_METHOD_NAME = "shutdown";

    private final Object bean;

    private final String beanName;

    /** 自定义的销毁方法名 */
    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        // 避免同时继承自DisposableBean，且自定义方法与DisposableBean方法同名，销毁方法执行两次的情况
        if (ObjectUtil.isNotEmpty(destroyMethodName) && !(bean instanceof DisposableBean && DESTROY_METHOD_NAME.equals(this.destroyMethodName))) {
            // 通过反射执行方法
            Method destroyMethod = ClassUtil.getPublicMethod(bean.getClass(), destroyMethodName);
            if (destroyMethod == null) {
                throw new BeansException("无法找到销毁方法名为 '" + destroyMethodName + "' 从bean为 '" + beanName + "'");
            }
            destroyMethod.invoke(bean);
        }
    }
}
