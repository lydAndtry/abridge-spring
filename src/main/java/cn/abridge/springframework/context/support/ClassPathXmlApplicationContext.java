package cn.abridge.springframework.context.support;

import cn.abridge.springframework.beans.BeansException;

import java.io.IOException;

/**
 * @Author: lyd
 * @Date: 2024/4/18 23:06
 * @Description:
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    public ClassPathXmlApplicationContext() {
    }

    public ClassPathXmlApplicationContext(String configLocation) throws BeansException, IOException {
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException, IOException {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
