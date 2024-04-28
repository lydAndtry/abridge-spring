package cn.abridge.springframework.context;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @Author: lyd
 * @Date: 2024/4/18 21:38
 * @Description: 这是一个SPI接口，大多数（如果不是所有）的应用上下文都应实现它。
 * 除了 {@link ApplicationContext} 接口中的应用上下文客户端方法外，还提供了配置应用上下文的功能。
 * <p>
 *     配置和生命周期方法在此被封装，以避免让它们在ApplicationContext客户端代码中显得过于明显。目前的方法应只被启动和关闭代码使用。
 * </p>
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 加载或刷新配置的持久化表示，这可能来自于基于Java的配置，XML文件，属性文件，关系数据库模式，或者其他格式。
     * <p>因为这是一个启动方法，如果它失败，应销毁已创建的单例，以避免资源悬挂。
     * 换句话说，调用此方法后，应实例化所有或者不实例化任何单例。</p>
     * @throws BeansException
     * @throws IllegalStateException
     */
    void refresh() throws BeansException, IllegalStateException;

    /**
     * 返回此应用上下文的内部bean工厂。可以用来访问底层工厂的特定功能。
     * @return 底层的bean工厂
     * @throws IllegalStateException
     */
    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
}
