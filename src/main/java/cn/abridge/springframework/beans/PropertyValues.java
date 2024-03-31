package cn.abridge.springframework.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lyd
 * @Date: 2024/3/28 21:52
 * @Description: Bean的属性值
 * 包含一个或多个 {@link PropertyValue} 对象的容器，通常包括针对特定目标 bean 的一次更新。
 * 这里与源码不同，源码中是个接口，由子类 {@code MutablePropertyValues} 来实现此接口，
 * 并且继承了Iterable<PropertyValue>。
 */
public class PropertyValues {
    /**
     * 存放属性值
     */
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    /**
     * 返回该对象中包含的PropertyValue对象的数组
     */
    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * 如果存在值，返回具有给定名称的属性值
     * @param propertyName 属性名
     * @return 属性值 或者 {@code null}
     */
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    /**
     * 将属性对象存入集合
     * @param pv 属性
     */
    public void addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
    }
}
