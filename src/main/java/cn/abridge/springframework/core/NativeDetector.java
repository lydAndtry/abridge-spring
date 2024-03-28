package cn.abridge.springframework.core;

/**
 * @Author: lyd
 * @Date: 2024/3/26 20:31
 * @Description: 用于检测GraalVM本机映像环境的通用委托。
 */
public abstract class NativeDetector {

    /** See <a href="https://github.com/oracle/graal/blob/master/sdk/src/org.graalvm.nativeimage/src/org/graalvm/nativeimage/ImageInfo.java">
     * github-graalvm
     * </a> */
    private static final boolean imageCode = (System.getProperty("org.graalvm.nativeimage.imagecode") != null);

    /**
     * 如果在映像构建上下文中或在映像运行时期间调用，返回 {@code true} ,反之返回 {@code false} 。
     */
    public static boolean inNativeImage() {
        return imageCode;
    }
}
