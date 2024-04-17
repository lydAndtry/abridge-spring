package cn.abridge.springframework.core.io;

import cn.abridge.springframework.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author: lyd
 * @Date: 2024/4/1 20:39
 * @Description: {@link ResourceLoader} 接口的默认实现。
 */
public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "地址不能为空");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            // 这里直接去掉前缀，所以在构造函数的时候就不需要进行前缀去除
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            try {
                // 尝试将路径解析为URL
                URL url = new URL(location);
                // 这里就不在做校验，直接返回
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                // URL无法解析资源路径, 当成文件系统下的资源处理
                return new FileSystemResource(location);
            }
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        // todo: 过去类加载器，目前暂时没实现，后续实现
        return null;
    }
}
