package cn.abridge.springframework.beans.factory.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.core.io.DefaultResourceLoader;
import cn.abridge.springframework.core.io.ResourceLoader;
import cn.abridge.springframework.util.Assert;
import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/4/1 21:16
 * @Description: 这是一个为实现 {@link BeanDefinitionReader} 接口的bean定义读取器提供的抽象基类。
 * <p>提供了常见的属性，如用于操作的bean工厂和用于加载bean类的类加载器。</p>
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    private final BeanDefinitionRegistry registry;

    @Nullable
    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader defaultResourceLoader) {
        this.registry = registry;
        this.resourceLoader = defaultResourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * 设置用于资源位置的ResourceLoader。如果指定了ResourcePatternResolver，
     * 那么bean定义读取器将有能力解析资源模式到Resource数组。
     * @param resourceLoader 资源加载器
     */
    public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        Assert.notNull(locations, "路径不能为空");
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }
}
