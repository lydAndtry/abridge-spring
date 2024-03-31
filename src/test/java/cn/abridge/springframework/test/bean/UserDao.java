package cn.abridge.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lyd
 * @Date: 2024/3/30 11:06
 * @Description:
 */
public class UserDao {
    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("1", "怒放吧德德");
        hashMap.put("2", "愤怒吧德德");
        hashMap.put("3", "爱国德德");
    }

    public String queryUserId(String id) {
        return hashMap.get(id);
    }
}
