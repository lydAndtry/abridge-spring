package cn.abridge.springframework.test.bean;

/**
 * @Author: lyd
 * @Date: 2024/3/19 20:54
 * @Description:
 */
public class UserService {
    private String id;

    private UserDao userDao;

    public void getUserInfo(){
        System.out.println("执行中。。。");
        System.out.println(userDao.queryUserId(id));
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
}
