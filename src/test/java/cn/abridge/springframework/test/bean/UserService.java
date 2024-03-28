package cn.abridge.springframework.test.bean;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:54
 * @Description:
 */
public class UserService {
    private String name;
    private String age;

    public UserService(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public void getUserInfo(){
        System.out.println("获取用户信息!!!" + name + " " + age);
    }
}
