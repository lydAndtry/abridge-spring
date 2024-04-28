package cn.abridge.springframework.context;

import cn.abridge.springframework.beans.factory.ListableBeanFactory;

/**
 * @Author: lyd
 * @Date: 2024/4/18 21:31
 * @Description: 应用上下文  为应用程序提供配置的中心接口。在应用程序运行时，这是只读的，但是如果实现支持，可重新加载。
 * <p>注：这里主要是继承关于 BeanFactory 方法，所以需要继承 ListableBeanFactory ,
 * 其他还需要继承其他接口，但是本次不实现。 </p>
 */
public interface ApplicationContext extends ListableBeanFactory {
}
