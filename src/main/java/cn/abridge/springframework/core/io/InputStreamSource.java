package cn.abridge.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 类的源对象的简单接口 {@link InputStream} 。
 *
 * <p>这是Spring更广泛的 {@link Resource}接口的基础接口。</p>
 *
 * 可以使用Spring的{@link ByteArrayResource}或任何基于文件的{@code Resource}实现作为具体的实例，
 * 这使得可以多次阅读底层的内容流。
 * 例如，这使得此接口作为邮件附件的抽象内容源非常有用。
 * @Author: lyd
 * @Date: 2024/3/31 21:18
 * @Description: 类的源对象的简单接口 {@link InputStream}
 */
public interface InputStreamSource {

    /**
     * 返回一个输入流
     * @return 输入流
     * @throws IOException 如果无法打开输入流
     */
    InputStream getInputStream() throws IOException;
}
