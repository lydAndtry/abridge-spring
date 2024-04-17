package cn.abridge.springframework.util;

import com.sun.istack.internal.Nullable;

/**
 * @Author: lyd
 * @Date: 2024/3/31 21:31
 * @Description:
 */
public abstract class ClassUtils {

    /**
     * 获取类加载其
     * 关于类加载器可以看一下这篇博客
     * <a href="https://blog.csdn.net/qq_43843951/article/details/133894758">【JVM系列】- 类加载子系统与加载过程</a>
     * @return 返回类加载器
     */
    @Nullable
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // 线程上下文类加载器获取不到
        }
        if (cl == null) {
            // 没有线程上下文类加载器->使用该类的类加载器。
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader()返回null表示引导类加载器。
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // 无法访问系统类加载器 - emmm，也许调用者可以接受null...
                }
            }
        }
        return cl;
    }
}
