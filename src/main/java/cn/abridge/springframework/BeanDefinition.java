package cn.abridge.springframework;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:40
 * @Description: bean定义，具有属性值、构造函数参数值。
 */
public class BeanDefinition {
    private Object bean;

    public BeanDefinition(Object bean) {
        this.bean = bean;
    }

    public Object getBean() {
        return bean;
    }
}
