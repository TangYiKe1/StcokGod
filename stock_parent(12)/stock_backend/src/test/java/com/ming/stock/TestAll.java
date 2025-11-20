package com.ming.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: Ming
 * @Description TODO
 */
@SpringBootTest
public class TestAll {
    @Autowired
    private PasswordEncoder passwordEncoder;
    /*
    * 测试密码加密
    * */
    @Test
    public void testPwd(){
        String password = "123456";
        //加密$2a$10$0u2G87kzkpkvyUbEuzjFY.jux.sCq41RLfwuBO6eUPS475ahU9GAW
        String encode = passwordEncoder.encode(password);
        System.out.println(encode);
        boolean flag = passwordEncoder.matches(password,
                "$2a$10$0u2G87kzkpkvyUbEuzjFY.jux.sCq41RLfwuBO6eUPS475ahU9GAW");
        System.out.println(flag);
    }
}
