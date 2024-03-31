package cn.abridge.springframework.beans;

import cn.abridge.springframework.util.Assert;
import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/3/28 21:45
 * @Description: 存储单个bean属性的信息和值
 */
public class PropertyValue {
    private final String name;

    @Nullable
    private final Object value;

    public PropertyValue(String name, @Nullable Object value) {
        Assert.notNull(name, "名称一定不能为空");
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
