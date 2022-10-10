package com.fgsqw;

import com.fgsqw.beans.user.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

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

    public String sendCodeMessage(RegisterUser user) throws Exception{
        String verifyCode = generateVerifyCode(6);
        // 创建一个邮件对象
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(username); // 设置发送人地址
        msg.setTo(user.getEmail()); // 设置接收方
        msg.setSubject("注册验证码"); // 设置邮件主题
        msg.setText(user.getUserName() + ",你好:\n"  + "本次注册验证码是：" + verifyCode + "(有效期一分钟),请及时输入!"); // 设置邮件内容
        // 发送邮件
        mailSender.send(msg);
        return verifyCode;
    }

    /**
     * 生成随机验证码
     * @param number 几位数
     * @return
     */
    private String generateVerifyCode(int number) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= number; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
