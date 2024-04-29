package cn.abridge.springframework.context.support;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import cn.abridge.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/4/18 22:47
 * @Description: 可刷新的应用程序上下文
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    @Nullable
    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        // 这里先只是创建bean工厂，加载bean定义，销毁和资源释放暂时没有涉及
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    /**
     * 将bean定义加载到给定的bean工厂中，通常是通过委托一个或多个bean定义阅读器来完成的。
     * @param beanFactory 用于加载bean定义的bean工厂
     * @throws BeansException
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
            throws BeansException;

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }
}
