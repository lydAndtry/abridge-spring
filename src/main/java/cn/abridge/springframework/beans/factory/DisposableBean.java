package cn.abridge.springframework.beans.factory;

/**
 * @Author: lyd
 * @Date: 2024/5/10 21:47
 * @Description: 销毁Bean
 */
public interface DisposableBean {
    /**
     * 销毁方法
     * @throws Exception /
     */
    void destroy() throws Exception;
}
