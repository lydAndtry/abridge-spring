package cn.abridge.springframework.beans.factory.xml;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.PropertyValue;
import cn.abridge.springframework.beans.factory.config.BeanDefinition;
import cn.abridge.springframework.beans.factory.config.BeanReference;
import cn.abridge.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import cn.abridge.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.abridge.springframework.core.io.Resource;
import cn.abridge.springframework.core.io.ResourceLoader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: lyd
 * @Date: 2024/4/1 21:25
 * @Description: 用于XML bean定义的bean定义读取器。
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader defaultResourceLoader) {
        super(registry, defaultResourceLoader);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        // 获取资源加载器
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        // 加载资源
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            // 获取输入流
            InputStream inputStream = resource.getInputStream();
            try {
                doLoadBeanDefinitions(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("解析XML文档[" + resource + "]出现 IOException", e);
        }
    }

    /**
     * 加载bean定义
     * @param inputStream 输入流
     */
    private void doLoadBeanDefinitions(InputStream inputStream) {
        // 通过hutool工具来读取xml
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        // 遍历解析xml文件元素
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node beanNode = childNodes.item(i);
            if (!(beanNode instanceof Element)) {
                continue;
            }
            // 不是bean的话也跳过
            if (!BEAN_ELEMENT.equals(beanNode.getNodeName())) {
                continue;
            }
            // 1 解析bean标签
            Element bean = (Element) beanNode;
            // 1.1 获取里面的参数
            String id = bean.getAttribute(ID_ATTRIBUTE);
            String name = bean.getAttribute(NAME_ATTRIBUTE);
            String className = bean.getAttribute(CLASS_ATTRIBUTE);
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            // 1.2 id的优先级比name高
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                // 在Spring中，默认是第一个子母小写开头为bean名。
                // 如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            // 1.3 创建bean定义
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            // 2 填充bean下的属性
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                Node propertyNode = bean.getChildNodes().item(j);
                // 过滤不是元素
                if (!(propertyNode instanceof Element)) {
                    continue;
                }
                // 不是property标签也过滤
                if (!PROPERTY_ELEMENT.equals(propertyNode.getNodeName())) {
                    continue;
                }
                // 2.2 解析property标签元素
                Element property = (Element) propertyNode;
                // 2.3 获取参数，属性名-属性值 ref: 标记是否为依赖Bean
                String nameAttribute = property.getAttribute(NAME_ATTRIBUTE);
                String valueAttribute = property.getAttribute(VALUE_ATTRIBUTE);
                String refAttribute = property.getAttribute(REF_ATTRIBUTE);
                if (StrUtil.isEmpty(nameAttribute)) {
                    throw new BeansException("属性名不能为空！");
                }
                Object value = valueAttribute;
                // 如果是个依赖的Bean对象，就实例化成bean引用对象。
                if (StrUtil.isNotEmpty(refAttribute)) {
                    value = new BeanReference(refAttribute);
                }
                // 2.4 创建PropertyValue对象，并加入属性容器
                PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {
                // beanName不能重名
                throw new BeansException("bean名[" + beanName + "] 不允许重复出现");
            }
            // 注册BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
