package cn.abridge.springframework.test.bean;

import cn.abridge.springframework.beans.BeansException;
import cn.abridge.springframework.beans.factory.DisposableBean;
import cn.abridge.springframework.beans.factory.InitializingBean;
import cn.abridge.springframework.context.ApplicationContext;
import cn.abridge.springframework.context.ApplicationContextAware;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:54
 * @Description:
 */
public class UserService implements InitializingBean, DisposableBean, ApplicationContextAware {
    private String id;

    private String address;

    private String tel;

    private UserDao userDao;

    public void getUserInfo(){
        System.out.println(userDao.queryUserId(id) + " 地址：" + address + " TEL: " + tel);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("userService通过实现[DisposableBean]的销毁方法，执行了destroy()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("userService通过实现[InitializingBean]的初始化方法，执行了afterPropertiesSet()");
    }

    public void initDataMethod() {
        System.out.println("userService另一个初始化方法");
    }
    public void destroyDataMethod(){
        System.out.println("userService另一个销毁方法");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("userService获得ApplicationContext：" + applicationContext);
    }
}
