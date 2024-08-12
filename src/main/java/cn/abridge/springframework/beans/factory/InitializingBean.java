package cn.abridge.springframework.beans.factory;

/**
 * @Author: lyd
 * @Date: 2024/5/10 21:43
 * @Description: 初始化Bean
 */
public interface InitializingBean {

    /**
     * 后置处理方法
     * @throws Exception /
     */
    void afterPropertiesSet() throws Exception;
}
