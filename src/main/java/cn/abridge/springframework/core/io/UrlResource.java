package cn.abridge.springframework.core.io;

import cn.abridge.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author: lyd
 * @Date: 2024/3/31 22:13
 * @Description:
 */
public class UrlResource extends AbstractFileResolvingResource {

    /**
     * 原始URL，用于实际访问。
     */
    private final URL url;

    public UrlResource(URL url) {
        Assert.notNull(url, "URL一定不能为空");
        this.url = url;
    }

    /**
     * 该实现为给定的URL打开一个输入流。
     */
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        try {
            return con.getInputStream();
        } catch (IOException ex) {
            // 如果启用，就关闭HTTP连接。
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
            throw ex;
        }
    }

}
