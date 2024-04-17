package cn.abridge.springframework.core.io;

import cn.abridge.springframework.util.ResourceUtils;
import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/4/1 20:36
 * @Description: 加载资源的策略接口(例如，类路径或文件系统资源)
 */
public interface ResourceLoader {
    /** 用于从类路径加载的伪URL前缀: "classpath:". */
    String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

    /**
     * 返回指定资源位置的资源{@code Resource}句柄。
     * <p>该句柄应始终是一个可重用的资源描述符，允许进行多次 {@link Resource#getInputStream()} 调用。
     * <p><ul>
     * <li>必须支持完全限定的URL, e.g. "file:C:/test.dat".
     * <li>必须支持类路径伪URL, e.g. "classpath:test.dat".
     * <li>应支持相对文件路径, e.g. "WEB-INF/test.dat".
     * (这将是特定于实现的，通常由ApplicationContext实现提供。)
     * </ul>
     * <p>请注意，资源 {@code Resource}句柄并不意味着资源的存在；
     * 你需要调用 {@link Resource#exists} 来检查资源是否存在。
     * @param location 资源位置
     * @return 相关的资源处理器，不会返回{@code null}
     */
    Resource getResource(String location);

    /**
     * 暴露此资源加载器{@code ResourceLoader}使用的类加载器{@link ClassLoader} 。
     * @return {@code ClassLoader}
     */
    @Nullable
    ClassLoader getClassLoader();
}
