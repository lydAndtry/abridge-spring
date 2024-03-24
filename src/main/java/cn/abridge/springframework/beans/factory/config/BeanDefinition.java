package cn.abridge.springframework.beans.factory.config;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:40
 * @Description: bean定义，具有属性值、构造函数参数值。
 */
public class BeanDefinition {
    private Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
