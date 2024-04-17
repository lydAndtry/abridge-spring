package cn.abridge.springframework.core.io;

import cn.abridge.springframework.util.Assert;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * @Author: lyd
 * @Date: 2024/3/31 21:47
 * @Description: 将url解析为文件引用的资源的抽象基类, 例如：{@link UrlResource} or {@link ClassPathResource}。
 * 注意：这里在Spring源码中还会实现WritableResource类，用来提供OutputStream访问器
 */
public class FileSystemResource extends AbstractResource {
    private final String path;

    @Nullable
    private final File file;

    private final Path filePath;

    public FileSystemResource(String path) {
        Assert.notNull(path, "路径一定不能为空");
        this.path = path;
        this.file = new File(path);
        this.filePath = file.toPath();
    }

    public FileSystemResource(File file) {
        Assert.notNull(file, "文件一定不能为空");
        this.file = file;
        this.path = file.getPath();
        this.filePath = file.toPath();
    }

    /**
     * 这个实现为底层文件打开一个NIO文件流。
     * @see java.nio.file.Files#newInputStream(Path, java.nio.file.OpenOption...)
     */
    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return Files.newInputStream(this.filePath);
        }
        catch (NoSuchFileException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }
}
