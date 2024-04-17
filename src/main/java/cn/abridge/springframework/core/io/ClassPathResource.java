package cn.abridge.springframework.core.io;

import cn.abridge.springframework.util.Assert;
import cn.abridge.springframework.util.ClassUtils;
import com.sun.istack.internal.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: lyd
 * @Date: 2024/3/31 21:24
 * @Description:
 */
public class ClassPathResource extends AbstractFileResolvingResource {
    private final String path;

    @Nullable
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path 一定不能为空");
        this.path = path;
        // 初始化classLoader的时候可以为null
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is;
        // 获取输入流，在Spring源码中，还需要使用本类获取，这里暂时不定义
        if (this.classLoader != null) {
            is = classLoader.getResourceAsStream(path);
        } else {
            is = ClassLoader.getSystemResourceAsStream(path);
        }
        if (is == null) {
            // 如果输入流还是空，抛出异常
            throw new FileNotFoundException(getDescription() + " 不存在，无法打开。");
        }
        return is;
    }

    private String getDescription() {
        StringBuilder builder = new StringBuilder("类资源路径 [");
        builder.append(this.path);
        builder.append(']');
        return builder.toString();
    }

}
