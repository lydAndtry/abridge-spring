# 第六章、应用上下文-ApplicationContext
## 1 前言
再上一章中，介绍了Spring是如何读取资源文件的，并且通过XML来定义Bean并注册Bean定义。这章就来讲解应用上下文，通过一些容器扩展机制，来完成对Bean对象的增强，这样就能够更进一步的了解Spring框架。
## 2 思路
Spring的应用上下文(ApplicationContext)是基于控制反转（IoC, Inversion of Control）以及依赖注入（DI，Dependency Injection）这两个核心思路和原则设计和实现的。这里涉及了两个部分，一个是Spring的容器扩展机制(`BeanFactoryPostProcessor`和`BeanPostProcessor`)，另一个是有了应用上下文，能自动识别**BeanFactoryPostProcessor**和**BeanPostProcessor**，就可以在xml文件中配置，我们就不必自己创建**BeanFactory**。
## 3 实践
本次使用的demo类的结构如下，代码可以看git的v6分支
```text
cn.abridge.springframework
	├─beans
	│  │  BeansException.java
	│  │  PropertyValue.java
	│  │  PropertyValues.java
	│  │  
	│  └─factory
	│      ├─BeanFactory.java
	│      ├─HierarchicalBeanFactory.java
	│      ├─ListableBeanFactory.java
	│      │  
	│      ├─config
	│      │   ├─AutowireCapableBeanFactory.java
	│      │   ├─BeanDefinition.java
	│      │   ├─BeanFactoryPostProcessor.java
	│      │   ├─BeanPostProcessor.java
	│      │   ├─BeanReference.java
	│      │   ├─ConfigurableBeanFactory.java
	│      │   ├─ConfigurableListableBeanFactory.java
	│      │   └─SingletonBeanRegistry.java
	│      │      
	│      ├─support
	│      │   ├─AbstractAutowireCapableBeanFactory.java
	│      │   ├─AbstractBeanDefinitionReader.java
	│      │   ├─AbstractBeanFactory.java
	│      │   ├─BeanDefinitionReader.java
	│      │   ├─BeanDefinitionRegistry.java
	│      │   ├─CglibSubclassingInstantiationStrategy.java
	│      │   ├─DefaultListableBeanFactory.java
	│      │   ├─DefaultSingletonBeanRegistry.java
	│      │   ├─InstantiationStrategy.java
	│      │   └─SimpleInstantiationStrategy.java
	│      │      
	│      └─xml
	│          └─XmlBeanDefinitionReader.java
	│              
	├─context
	│  ├─ApplicationContext.java
	│  ├─ConfigurableApplicationContext.java
	│  │  
	│  └─support
	│      ├─AbstractApplicationContext.java
	│      ├─AbstractRefreshableApplicationContext.java
	│      ├─AbstractXmlApplicationContext.java
	│      └─ClassPathXmlApplicationContext.java
	│          
	├─core
	│  ├─NativeDetector.java
	│  │  
	│  └─io
	│     ├─AbstractFileResolvingResource.java
	│     ├─AbstractResource.java
	│     ├─ClassPathResource.java
	│     ├─DefaultResourceLoader.java
	│     ├─FileSystemResource.java
	│     ├─InputStreamSource.java
	│     ├─Resource.java
	│     ├─ResourceLoader.java
	│     └─UrlResource.java
	│          
	└─util
	    ├─Assert.java
	    ├─ClassUtils.java
	    ├─CollectionUtils.java
	    └─ResourceUtils.java
```
### 3.1 类图
![ConfigurableBeanFactory.png](https://cdn.nlark.com/yuque/0/2024/png/32574087/1713671930633-ffe0f50d-dd94-4b1f-90f2-7cd35dc3d25a.png#averageHue=%232a2f38&clientId=u4c966f6f-863b-4&from=paste&height=2611&id=uf65de5a1&originHeight=3264&originWidth=4226&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=433709&status=done&style=none&taskId=ud296ffea-9b99-4473-8a03-3adb40a6343&title=&width=3380.8)<br />在Spring框架中，有着两个重量级接口：BeanFactoryPostProcessor和BeanPostProcessor，这两个都是Spring提供的容器扩展机制，BeanFactoryPostProcessor是在Bean实例化之前来修改Bean定义信息，在刷新bean容器的时候会执行；BeanPostProcessor是Bean的前后置处理，主要是在Bean已经实例化之后的处理，他是后面实现AOP的关键，在创建bean实例后，会执行一段初始化Bean，会来进行对Bean的前置处理和后置处理。<br />接下来就是引入了应用上下文(ApplicationContext)，它实现了能够自动解析XML并转换成Bean对象，途中我们就不需要进行手动去创建BeanFactory。
### 3.2 定义容器扩展机制
Spring框架提供了强大的容器扩展机制，允许在Spring的IoC容器进行初始化或刷新之后，执行特定的动作。这个功能主要是通过实现特定的回调接口或通过声明自定义的事件来实现的。然而本章主要学习的是BeanFactoryPostProcessor和BeanPostProcessor两个机制，这两个主要是在bean实例化前的处理和对bean实例化之后的前后置处理。<br />本章主要学习的是BeanFactoryPostProcessor和BeanPostProcessor两个机制：

- **BeanPostProcessor**：这是最常用的扩展机制，可以在bean的初始化阶段进行额外的处理。当IoC容器实例化一个bean之后，它将检查这个bean是否实现了BeanPostProcessor接口，如果实现了，Spring将会在调用初始化方法（如初始化回调方法，或者标记了@PostConstruct的方法）之前和之后，调用BeanPostProcessor的postProcessBeforeInitialization和postProcessAfterInitialization方法。
- **BeanFactoryPostProcessor**：这个接口允许我们在Spring IoC容器实例化任何bean之前，对bean的定义（也就是BeanDefinition）进行修改。它的主要用途是在bean实例化之前修改bean的配置元数据。
#### 3.2.1 BeanFactoryPostProcessor
`BeanFactoryPostProcessor`是一个工厂钩子，允许自定义修改应用上下文的bean定义，适应上下文底层bean工厂的bean属性值。<br />`BeanFactoryPostProcessor`是Spring框架中一个非常重要的接口，它允许我们在Spring的IoC容器实例化任何bean之前，对bean的定义（也就是BeanDefinition）进行修改。这个接口主要用于修改bean的配置元数据。<br />当你定义一个实现了`BeanFactoryPostProcessor`接口的类并注册到容器中时，Spring IoC容器会在所有BeanDefinition加载完成，但所有bean实例化之前，调用这个接口的 postProcessBeanFactory() 方法。
```java
public interface BeanFactoryPostProcessor {
    /**
     * @param beanFactory 应用上下文使用的bean工厂
     * @throws BeansException 在出现错误的情况下。
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
```
这里就是提供了一个postProcessBeanFactory()方法，通过实现这个方法，能够在实例化bean之前进行修改bean信息，如下demo：
```java
public class BeanFactoryPostProcessorTest implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println(this.getClass().getName() + "---未实例化前的处理>>> ");
        BeanDefinition userServiceBeanDefinition = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = userServiceBeanDefinition.getPropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("tel", "86-0595"));
    }
}
```
这个demo实现了`BeanFactoryPostProcessor`接口，可以通过bean工厂获取bean定义，这里是指定了对应的bean定义，并且通过设置填充属性来完成对bean信息的修改。
> 注：BeanFactoryPostProcessor操作的是BeanDefinition对象，这是bean的配置数据，而不是bean实例。如果你需要在bean实例创建后，初始化前进行一些处理，那么你应该使用BeanPostProcessor接口。

#### 3.2.2 BeanPostProcessor
`BeanPostProcessor`是工厂钩子，允许对新的bean实例进行自定义修改。例如，检查标记接口或用代理包装beans。<br />`BeanPostProcessor`是Spring框架中一个非常重要的接口，它为我们提供了一种在Spring容器完成bean的实例化、配置和初始化之后，添加自定义逻辑的手段。<br />具体来说，当Spring IoC容器实例化一个bean之后，它将检查这个bean是否实现了`BeanPostProcessor`接口，如果实现了，Spring将会在调用初始化方法（如初始化回调方法，标注@PostConstruct的方法）之前和之后，调用**BeanPostProcessor**的**postProcessBeforeInitialization**和**postProcessAfterInitialization**方法。
```java
public interface BeanPostProcessor {

    /**
     * 在Bean对象初始化之前执行
     * @param bean 新的bean实例
     * @param beanName bean的名称
     * @return 要使用的bean实例，可以是原始的或者是封装后的；
     * 如果是{@code null}，则不会调用后续的 BeanPostProcessors。
     * @throws BeansException 出现错误的时候
     */
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 在Bean对象初始化之后执行
     * @param bean 新的bean实例
     * @param beanName bean的名称
     * @return 要使用的bean实例，可以是原始的或者是封装后的；
     * @throws BeansException 出现错误的时候
     */
    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
```
**postProcessBeforeInitialization**: 该方法在Spring在bean的所有初始化方法执行之前调用。可以在这个阶段修改我们的bean, 或者返回一个全新的bean。<br />**postProcessAfterInitialization**: 该方法在Spring在bean的所有初始化方法执行之后调用。同样的，可以在这修改我们的bean, 或者返回一个全新的bean。
### 3.3 Bean的前置和后置处理
现在，我们已经对BeanFactoryPostProcessor和BeanPostProcessor这两个接口进行了定义，可以通过实现这两个接口，接着在Spring Bean的生命周期中介入。<br />接着需要先了解两个接口，分别是ConfigurableListableBeanFactory和ConfigurableBeanFactory。
:::success
ConfigurableBeanFactory 提供了对Bean的基础管理和配置，而 ConfigurableListableBeanFactory 接口则在此基础上添加了更多的检索和管理方法，给Spring的IoC提供关键基础。
:::
#### 3.3.1 ConfigurableBeanFactory
> ConfigurableBeanFactory 是Spring框架中所有BeanFactory的基础接口，提供了诸如Bean的定义、创建、范围、工厂等配置功能。此接口由几乎所有的BeanFactory实现，包括 XmlBeanFactory 和 DefaultListableBeanFactory。
> 一些主要的操作包括对Bean的生命周期管理(addBeanPostProcessor,registerScope等)，以及对Bean的创建(createBean)，注入(resolveDependency)和配置(mergeBeanDefinition,registerAlias等)。
> 目前这个类只是提供了addBeanPostProcessor方法。

本次就先提供一个addBeanPostProcessor方法，目的是向 Spring IoC 容器中添加一个新的 BeanPostProcessor。
```java
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
    /**
     * 添加一个新的BeanPostProcessor，它将应用于由此工厂创建的beans。该操作应在工厂配置期间被调用。
     * @param beanPostProcessor /
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
```
如上的方法，最终是由`抽象类：AbstractBeanFactory`来实现完成，代码如下：
```java
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor, "BeanPostProcessor 不能为空");
        // 有就覆盖
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
    // 忽略其他方法...
}
```
这段代码是与Spring框架一样的，只是没有做同步处理。主要就是将每个BeanPostProcessor添加到BeanPostProcessor容器中，如果容器中已经包含，那就将其覆盖。
#### 3.3.2 AutowireCapableBeanFactory
`AutowireCapableBeanFactory`是`BeanFactory`的扩展接口，还是Spring框架中Bean创建和自动装配的核心接口，适用于能够进行自动装配的bean工厂，只要他们希望将此功能暴露给现有的bean实例。<br />这个接口添加了一些在 BeanFactory 中不存在的工厂相关操作，比如创建bean，自动装配特定bean属性，初始化bean，应用前后处理器等。而在本章就只是定义了应用前后置处理器。
```java
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 应用前置处理， 调用BeanPostProcessor的postProcessBeforeInitialization方法
     * @param existingBean
     * @param beanName
     * @return 使用的bean实例，可以是原始的，也可以是一个包装过的。
     * @throws BeansException
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;
    /**
     * 应用后置处理， 调用它们的postProcessAfterInitialization方法。
     * @param existingBean
     * @param beanName
     * @return 使用的bean实例，可以是原始的，也可以是一个包装过的。
     * @throws BeansException
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;
}
```
这里就是本次所需要的两个接口，applyBeanPostProcessorsBeforeInitialization() 和 applyBeanPostProcessorsAfterInitialization()，这两个方法允许对bean进行前处理和后处理。
#### 3.3.3 ConfigurableListableBeanFactory
`ConfigurableListableBeanFactory`也是Bean工厂的配置接口，除了继承了`ConfigurableBeanFactory`，自身还提供了分析和修改bean定义，以及预先实例化单例的功能。
> ConfigurableListableBeanFactory 接口继承自 ConfigurableBeanFactory 并添加了针对bean检索的功能。除了以名称方式检索bean（通过 getBean 方法），它也支持按类型检索bean（通过getBeansOfType等方法）。
> 此外，ConfigurableListableBeanFactory 还可以预加载所有的singleton beans （通过 preInstantiateSingletons 方法），也可以决定是否允许Bean之间的循环依赖。

:::info
继承了`AutowireCapableBeanFactory`，主要是为了继承`BeanFactory`接口。
:::
```java
public interface ConfigurableListableBeanFactory
        extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {
    /**
     * 抢先实例化bean，
     * 确保所有非懒加载的单例都被实例化
     * @throws BeansException 创建bean发生异常
     */
    void preInstantiateSingletons() throws BeansException;
    // 忽略其他方法...
}
```
为了确保所有的单例都被实例化，ConfigurableListableBeanFactory接口提供了preInstantiateSingletons()方法，用来创建单例Bean。最终是通过DefaultListableBeanFactory来实现。
```java
@Override
public void preInstantiateSingletons() throws BeansException {
    beanDefinitionMap.keySet().forEach(this::getBean);
}
```
这里就不像源码那么复杂，毕竟是删减版嘛，主要是为了了解spring的框架整体思路，这里就是直接遍历BeanDefinition容器中的对象，执行getBean去创建单例bean。
#### 3.3.4 AbstractAutowireCapableBeanFactory
接下来就是这个(**AbstractAutowireCapableBeanFactory**)抽象类，它是一个很关键的类，是Spring的核心容器的一部分，这个类主要负责去生产bean实例，包括bean的定义属性和注入依赖。对比之前分支上的代码，这里多了实现`AutowireCapableBeanFactory`接口，这也就是前几章都没做到的，现在慢慢地去完善，然而本次还不是很完善，就是简单的对bean进行初始化，去应用前后置处理。<br />首先需要定义初始化bean方法(initializeBean)，主要是对实例化之后的bean进行前后置处理，以及对其执行初始化方法(这部分后续完成)。这段代码会在实例化完bean对象后，包括属性填充、依赖注入完成之后调用。
```java
private Object initializeBean(String beanName, Object bean, BeanDefinition mbd) {
    Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
    // todo 后面会在此处执行bean的初始化方法
    invokeInitMethods(beanName, wrappedBean, mbd);
    wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
    return wrappedBean;
}
```
接下来就是实现需要调用的应用前后置处理器，这是由`AutowireCapableBeanFactory`接口定义的两个方法。
```java
@Override
public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
    Object result = existingBean;
    for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
        Object current = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
        if (current == null) {
            return result;
        }
        return current;
    }
    return result;
}

@Override
public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
    Object result = existingBean;
    for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
        Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
        if (current == null) {
            return result;
        }
        return current;
    }
    return result;
}
```
在这小节上面已经提到了Spring是将BeanPostProcessor（处理器）缓存到AbstractBeanFactory类中的容器中，所以这里会通过父类(AbstractBeanFactory)的getBeanPostProcessors方法获取所有的Bean前后置处理器对象，并且执行对应的处理（这里需要由对应的处理器去实现方法，并且返回的是bean对象）。在Spring框架中，对于BeanPostProcessor接口暴露的前后置处理方法，其返回了默认的bean实例，也就是如果这个Bean没有对应的处理器，那么将会返回这个已经实例的Bean。
### 3.4 应用上下文
> 接下来就是引入ApplicationContext，能自动识别BeanFactoryPostProcessor和BeanPostProcessor，就可以在xml文件中配置而不需要手动添加到BeanFactory了。

ApplicationContext是Spring框架的核心接口，它是BeanFactory的子接口。这个接口的主要作用是提供一个容器（也可以称之为上下文）来持有和管理不同的Spring beans。不仅能够自动识别BeanFactoryPostProcessor和BeanPostProcessor，还能够支持国际化（例如消息束消息的处理）、事件传播和各种应用层面的上下文。<br />![未命名文件 (33).png](https://cdn.nlark.com/yuque/0/2024/png/32574087/1713706218038-0361ab29-90b2-434e-be90-fbd311e528f7.png#averageHue=%23f8f8f8&clientId=u49134873-6fb4-4&from=paste&height=566&id=u15a0d619&originHeight=707&originWidth=901&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=54808&status=done&style=none&taskId=u06196c34-692b-4a7b-8a87-42ca70cec99&title=&width=720.8)<br />todo

#### 3.4.1 上下文接口定义
##### 3.4.1.1 ApplicationContext
```java
public interface ApplicationContext extends ListableBeanFactory {
}
```
`ApplicationContext`继承`ListableBeanFactory`，而`ListableBeanFactory`是`BeanFactory`的拓展，它提供了通过各种方式获取BeanDefinition，这里就提供一个接口`getBeansOfType()`通过bean类型来获取BeanDefinition，以及`getBeanDefinitionNames()`方法来获取此工厂中定义的所有beans的名称。
##### 3.4.1.2 ListableBeanFactory
```java
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
```
`ListableBeanFactory`：BeanFactory接口的扩展，由可以枚举所有bean实例的bean工厂实现，而不是按照客户端的请求逐一尝试通过名称查找bean。预加载所有bean定义的BeanFactory实现（比如基于XML的工厂）可以实现这个接口。<br />getBeansOfType()方法最终是由DefaultListableBeanFactory类来实现。
```java
@Override
public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
    Map<String, T> result = CollectionUtils.newLinkedHashMap(beanDefinitionMap.size());
    beanDefinitionMap.forEach((beanName, beanDefinition) -> {
        Class beanClass = beanDefinition.getBeanClass();
        if (type.isAssignableFrom(beanClass)) {
            T bean = (T) getBean(beanName);
            result.put(beanName, bean);
        }
    });
    return result;
}
```
如上代码，其实现也就是遍历BeanDefinition容器中的数据，根据传过来的类型去过滤。
##### 3.4.1.3 ConfigurableApplicationContext
`ConfigurableApplicationContext` 是 Spring 框架中的一个重要接口，它继承自 ApplicationContext，并添加了一些能够修改应用上下文的功能。它本质上是一个可以配置的 ApplicationContext。在此目前是提供了refresh和getBeanFactory方法。
> ConfigurableApplicationContext 是Spring框架的一个核心接口，它扩展了 ApplicationContext ，增加了一些可配置性。通常情况下，ApplicationContext 是一个只读的上下文，但是利用ConfigurableApplicationContext，我们可以改变这一点。

```java
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
    
    void refresh() throws BeansException, IllegalStateException;

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
}
```
这里需要了解一下refresh()方法，这个是Spring容器中的核心方法。这个方法的主要作用就是刷新、初始化Spring上下文。总的来说，refresh() 方法就是用来启动或者重新启动应用上下文的生命周期的。当你在应用程序中调用了 refresh() 方法后，Spring容器就可以正常地创建和管理Beans，并且提供依赖注入功能。
#### 3.4.2 应用上下文的抽象实现
介绍了应用上下文的相关接口的定义后，现在就是需要去实现。Spring对于这部分是拆分了许多的抽象类，设计了多层次的上下文继承体系，主要还是为了通过分层抽象确保每个类都只关注特定的职责，通过继承抽象类，共同的代码和行为可以被复用，这种层次结构会使得Spring容器更加灵活性和更好的可扩展性。
##### 3.4.2.1 AbstractApplicationContext
`AbstractApplicationContext`在 Spring 框架中扮演着核心的角色，它是一个抽象类，提供了 `ApplicationContext`接口的基本实现。ApplicationContext 是 Spring 的高级容器，比 BeanFactory 提供更多的功能。AbstractApplicationContext 是各种具体应用上下文类的基类，例如 ClassPathXmlApplicationContext、FileSystemXmlApplicationContext 等，这些都是在不同场景下使用的具体实现。
:::success
AbstractApplicationContext提供了几个核心职责，其中包括事件发布、资源加载、国际化、环境抽象、Bean定义读取与注册以及Bean生命周期得支持。
:::
这里只要是对Bean定义读取与注册和刷新上下文等操作。<br />1) 实现刷新上下文refresh()方法
```java
@Override
public void refresh() throws BeansException, IllegalStateException {
    // 实现刷新容器
    ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
    // 注册Bean实例化前的处理器
    invokeBeanFactoryPostProcessors(beanFactory);
    // 注册Bean前后置处理器
    registerBeanPostProcessors(beanFactory);
    // 实例化所有剩余的（非延迟初始化）单例。
    finishBeanFactoryInitialization(beanFactory);
}
```
该方法负责执行Spring应用上下文的刷新操作，其主要目的是重建Bean工厂，重新初始化和调整应用上下文环境。这里还有国际化处理、初始化事件发布器等的功能，目前还没有去实现。<br />2) 接着需要刷新容器，在obtainFreshBeanFactory方法中，需要由子类去实现刷新bean工厂，并且由子类实现获取bean工厂。具体refreshBeanFactory和getBeanFactory方法是由子类AbstractRefreshableApplicationContext来实现，这个后续会介绍。
```java
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
    refreshBeanFactory();
    return getBeanFactory();
}

@Override
public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;


protected abstract void refreshBeanFactory() throws BeansException;
```
`invokeBeanFactoryPostProcessors`方法是用来实例化并调用所有已注册的BeanFactoryPostProcessor beans。
> 必须在单例实例化之前调用。

```java
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
    for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
    }
}
```
这个方法首先会在SpringBean定义容器中获取BeanFactoryPostProcessor的Bean对象，方法是由DefaultListableBeanFactory实现的：
```java
@Override
public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
    Map<String, T> result = CollectionUtils.newLinkedHashMap(beanDefinitionMap.size());
    beanDefinitionMap.forEach((beanName, beanDefinition) -> {
        Class beanClass = beanDefinition.getBeanClass();
        if (type.isAssignableFrom(beanClass)) {
            T bean = (T) getBean(beanName);
            result.put(beanName, bean);
        }
    });
    return result;
}
```
得到BeanFactoryPostProcessor集合之后，通过遍历BeanFactoryPostProcessor集合，就能够执行对应的未实例化前的处理，我们就可以对bean的定义进行修改，这就是Spring提供的一种非常强大的扩展点。<br />3) 在实例化前执行了对bean定义的修改之后，就是需要给Bean工厂注册BeanPostProcessor前后处理器，它将应用于由此工厂创建的beans，该操作应在工厂配置期间被调用。
```java
protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
    for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
        beanFactory.addBeanPostProcessor(beanPostProcessor);
    }
}
```
这个方法也是在Bean容器中获取BeanPostProcessor的Bean对象，并对bean工厂添加BeanPostProcessor。这个的作用就是为了在实例化之后进行对Bean的初始化的时候能够获取到对应的BeanPostProcessor，再去应用前后置处理器，最后执行实现的处理。<br />4) 注册bean前后置处理器之后呢，就需要进行实例化所有剩余的（非延迟初始化）单例。
```java
protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
    // 目前只做一件事：提前实例化单例bean
    beanFactory.preInstantiateSingletons();
}
```
这个方法是最终由DefaultListableBeanFactory去实现
```java
 @Override
public void preInstantiateSingletons() throws BeansException {
    beanDefinitionMap.keySet().forEach(this::getBean);
}
```
遍历bean定义容器去执行创建单例bean。
##### 3.4.2.2 AbstractRefreshableApplicationContext
AbstractRefreshableApplicationContext 是 Spring 框架中的一个抽象类，是 ApplicationContext 接口实现的一部分，主要负责对 BeanFactory 的管理和刷新。它继承自 AbstractApplicationContext，并实现了 ConfigurableApplicationContext 和 ResourcePatternResolver 接口，增加了可自定义资源路径解析和可刷新的特性。
:::info
此类中定义了部分核心逻辑。最重要的是，它提供了 refreshBeanFactory() 和 getBeanFactory() 方法的实现，以及 closeBeanFactory() 方法的默认实现，使ApplicationContext有能力管理和操作Spring的BeanFactory。
:::
```java
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    @Nullable
    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

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
```
这里主要的是这个刷新bean工厂的，通过创建一个DefaultListableBeanFactory，并且将bean定义加载到给定的bean工厂中，这个是通过指定的bean定义阅读器加载。
##### 3.4.2.3 AbstractXmlApplicationContext
AbstractXmlApplicationContext是Spring框架中用于加载XML定义的beans的一个抽象类。它提供了对XML资源的bean定义的加载和解析，并继承自AbstractRefreshableApplicationContext类。
```java
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.setResourceLoader(this);

        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException {
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            reader.loadBeanDefinitions(configLocations);
        }
    }

    /**
     * 获取配置路径
     * @see ClassPathXmlApplicationContext
     * @return 路径数组
     */
    protected abstract String[] getConfigLocations();

}
```
`AbstractXmlApplicationContext`重写了`loadBeanDefinitions()`方法，以处理XML配置文件里的bean定义。它利用Spring的`BeanDefinitionReader`，具体来说是`XmlBeanDefinitionReader`来解析XML配置文件里的bean定义（具体是如何读取XML解析Bean的，在上一章就有介绍过，代码可以看[v5分支](https://gitee.com/liyongde/abridge-spring/tree/v5/)），而且它需要子类提供`getConfigResources()`和`getConfigLocations()`这两个方法的具体实现。前者返回Resource对象数组，后者返回用于加载bean定义的配置文件的位置路径数组。<br />因为我们是通过XML来配置Bean信息，所以这里需要获取XML资源定义读取器。最后通过资源加载器去加载XML资源。
#### 3.4.3 XML加载应用上下文
`ClassPathXmlApplicationContext`是暴露给用户使用的类，它继承了AbstractXmlApplicationContext，最终是涵盖着应用上下文的抽象类以及接口。
```java
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    public ClassPathXmlApplicationContext() {
    }

    public ClassPathXmlApplicationContext(String configLocation) throws BeansException, IOException {
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException, IOException {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
```
在创建 ClassPathXmlApplicationContext 实例时，构造完成后会自动调用 refresh() 方法，初始化 Spring 上下文环境。需要注意的是，一旦 refresh() 被调用，就不能再向 ApplicationContext 中添加更多的 BeanFactoryPostProcessor 或 PropertySource。
## 4 测试
### 4.1 前期准备
首先，我们先定义好XML配置文件。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
  <bean id="userDao" class="cn.abridge.springframework.test.bean.UserDao"/>

  <bean id="userService" class="cn.abridge.springframework.test.bean.UserService">
    <property name="id" value="1"/>
    <property name="userDao" ref="userDao"/>
    <property name="address" value="浙江杭州"/>
    <property name="tel" value="86-0571"/>
  </bean>

  <bean class="cn.abridge.springframework.test.common.BeanFactoryPostProcessorTest"/>
  <bean class="cn.abridge.springframework.test.common.BeanPostProcessorTest"/>
</beans>
```
定义了两个我们即将使用实例化前和实例化后的前后置处理去修改里面的信息，默认adderss=浙江杭州，tel=06-0571。<br />接着，我们需要先去实现BeanFactoryPostProcessor和BeanPostProcessor。<br />实现BeanFactoryPostProcessor，实例化前，我们进行改变Bean的属性tel为86-0595。
```java
public class BeanFactoryPostProcessorTest implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println(this.getClass().getName() + "---未实例化前的处理>>> ");
        BeanDefinition userServiceBeanDefinition = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = userServiceBeanDefinition.getPropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("tel", "86-0595"));
    }
}
```
实现BeanPostProcessor前后置处理，这个一定要在Bean实例化之后执行的。我们只是在前置处理进行对Bean的属性address修改成福建泉州。后置处理就只是打印了一句话。
```java
public class BeanPostProcessorTest implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(this.getClass().getName() + "---初始化前置处理>>> beanName: " + beanName);
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setAddress("福建泉州");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(this.getClass().getName() + "---初始化后置处理>>> beanName: " + beanName);
        return bean;
    }
}
```
### 4.2 不使用上下文
首先我们测试的是，当我们不使用ClassPathXmlApplicationContext去实现，而是采用手动一步一步去完成。 
```java
@Test
public void test_BeanPostProcessor() {
    // 1 获取默认的Bean工厂
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    // 2 解析XML, 注册Bean
    XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
    // 3.1 执行Bean实例化前置处理
    BeanFactoryPostProcessorTest beanFactoryPostProcessorTest = new BeanFactoryPostProcessorTest();
    beanFactoryPostProcessorTest.postProcessBeanFactory(beanFactory);
    // 3.2 实例化之后的处理
    BeanPostProcessorTest beanPostProcessorTest = new BeanPostProcessorTest();
    beanFactory.addBeanPostProcessor(beanPostProcessorTest);
    // 4 获取bean对象
    UserService userService = beanFactory.getBean("userService", UserService.class);
    userService.getUserInfo();
}
```
实现起来就是我们需要根据对应的逻辑来处理，首先我们需要获取默认Bean工厂，通过地址解析XML来注册Bean，再来执行Bean实例化之前的处理，等实例化后再来执行前后置处理。<br />输出结果
```java
cn.abridge.springframework.test.common.BeanFactoryPostProcessorTest---未实例化前的处理>>> 
cn.abridge.springframework.test.common.BeanPostProcessorTest---初始化前置处理>>> beanName: userDao
cn.abridge.springframework.test.common.BeanPostProcessorTest---初始化后置处理>>> beanName: userDao
cn.abridge.springframework.test.common.BeanPostProcessorTest---初始化前置处理>>> beanName: userService
cn.abridge.springframework.test.common.BeanPostProcessorTest---初始化后置处理>>> beanName: userService
怒放吧德德 地址：福建泉州 TEL: 86-0595
```
### 4.3 引入应用上下文
没有使用应用上下文需要自己去在不同的阶段调用实现的扩展机制，然而，当我们使用了上下文机制，这里就会在上下文(ClassPathXmlApplicationContext)实例化的时候去执行refresh刷新容器，会通过bean工厂去注册实例化前置处理器以及注册实例化后的前后置处理器，接着进行提前实例化单例bean。这里看似简单，其实内部并不简单。
```java
@Test
public void test_ApplicationContext() throws IOException {
    // 1. 获取上下文应用
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
    // 2. 获取Bean对象调用方法
    UserService userService = applicationContext.getBean("userService", UserService.class);
    userService.getUserInfo();
}
```
执行结果
```java
cn.abridge.springframework.test.common.BeanFactoryPostProcessorTest$$EnhancerByCGLIB$$520f7b8f---未实例化前的处理>>> 
cn.abridge.springframework.test.common.BeanPostProcessorTest$$EnhancerByCGLIB$$3fbc726b---初始化前置处理>>> beanName: userDao
cn.abridge.springframework.test.common.BeanPostProcessorTest$$EnhancerByCGLIB$$3fbc726b---初始化后置处理>>> beanName: userDao
cn.abridge.springframework.test.common.BeanPostProcessorTest$$EnhancerByCGLIB$$3fbc726b---初始化前置处理>>> beanName: userService
cn.abridge.springframework.test.common.BeanPostProcessorTest$$EnhancerByCGLIB$$3fbc726b---初始化后置处理>>> beanName: userService
怒放吧德德 地址：福建泉州 TEL: 86-0595
```
## 5 总结
本章节学习了通过BeanFactoryPostProcessor和BeanPostProcessor来完成对Bean对象的增强，并且通过应用上下文进行解析XML，并执行对Bean的前置处理。通过源码的形式一步一步了解到ClassPathXmlApplicationContext是如何做到通用的扩展bean定义信息。
