package com.fgsqw;

import com.fgsqw.beans.user.RegLogUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;

    public void sendMessage(String to, String subject, String content) {
        // 创建一个邮件对象
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(username); // 设置发送人地址
        msg.setTo(to); // 设置接收方
        msg.setSubject(subject); // 设置邮件主题
        msg.setText(content); // 设置邮件内容
        // 发送邮件
        mailSender.send(msg);
    }

    public void sendCodeMessage(RegLogUser user) {
        // 创建一个邮件对象
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(username); // 设置发送人地址
        msg.setTo(user.getEmail()); // 设置接收方
        msg.setSubject("注册验证码"); // 设置邮件主题
        msg.setText(user.getUserName() + ",你好:\n"  + "本次注册验证码是：" + user.getVerifyCode() + "(有效期一分钟),请及时输入!"); // 设置邮件内容
        // 发送邮件
        mailSender.send(msg);
    }
}
