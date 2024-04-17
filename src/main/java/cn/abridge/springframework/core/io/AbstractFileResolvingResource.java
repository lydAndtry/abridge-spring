package cn.abridge.springframework.core.io;

/**
 * @Author: lyd
 * @Date: 2024/4/11 23:04
 * @Description:
 */
public abstract class AbstractFileResolvingResource extends AbstractResource {

    @Override
    public boolean exists() {
        // todo: 暂时不实现
        return super.exists();
    }
}
