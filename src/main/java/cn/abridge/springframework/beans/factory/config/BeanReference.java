package cn.abridge.springframework.beans.factory.config;

/**
 * @Author: lyd
 * @Date: 2024/3/28 22:24
 * @Description: Bean引用
 * <p>这里就是简单实现构造方式设置bean名称，通过getBeanName获取依赖的bean名称。</p>
 * <p>在Spring源码中是以抽象方式公开对 bean 名称的引用的接口。此接口不一定意味着对实际 bean 实例的引用；
 * 它只是表达对 bean 名称的逻辑引用。</p>
 */
public class BeanReference {

    private final String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
