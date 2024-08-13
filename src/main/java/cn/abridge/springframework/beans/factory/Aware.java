package cn.abridge.springframework.beans.factory;

/**
 * @Author: lyd
 * @Date: 2024/8/13 20:54
 * @Description: 标记接口
 * <p>一个标记超接口，指示bean有资格通过回调样式的方法由特定框架对象的Spring容器通知。
 * 实际的方法签名由各个子接口决定，但通常应该只包含一个接受单个参数的空返回方法。</p>
 */
public interface Aware {
}
