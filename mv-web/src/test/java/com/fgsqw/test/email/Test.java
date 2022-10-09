package com.fgsqw.test.email;

import com.fgsqw.EmailUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

//    @Autowired
//    private JavaMailSender mailSender;
    @Resource
    private EmailUtil emailUtil;

    @org.junit.Test
    public void test(){
        emailUtil.sendMessage("1710885741@qq.com","测试","测试");
    }
}
