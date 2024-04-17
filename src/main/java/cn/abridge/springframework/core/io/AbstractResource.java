package cn.abridge.springframework.core.io;

/**
 * @Author: lyd
 * @Date: 2024/4/11 23:03
 * @Description:
 */
public abstract class AbstractResource implements Resource {

    @Override
    public boolean exists() {
        // todo: 暂时不实现
        return false;
    }
}
