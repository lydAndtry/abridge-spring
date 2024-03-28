package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * @Author: lyd
 * @Date: 2024/3/25 21:28
 * @Description: Cglib子类实例化策略
 * <p>生成CGLIB对象</p>
 */
public class CglibSubclassingInstantiationStrategy extends SimpleInstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition bd, String beanName, Constructor ctor, Object... args) throws BeansException {
        // CGLIB的增强类对象
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bd.getBeanClass());
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        if (null == ctor) {
            // 返回的是无参
            return enhancer.create();
        }
        return enhancer.create(ctor.getParameterTypes(), args);
    }
}
