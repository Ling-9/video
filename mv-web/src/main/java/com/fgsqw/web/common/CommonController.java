package com.fgsqw.web.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fgsqw.EmailUtil;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.result.ResultCodeEnum;
import com.fgsqw.beans.user.RegisterUser;
import com.fgsqw.iservice.redis.IRedisCacheService;
import com.fgsqw.iservice.user.IMvUserService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private IRedisCacheService cacheService;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private IMvUserService userService;

    @ApiOperation(value = "图形验证码")
    @GetMapping(value = "/captcha", produces = "image/jpeg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        // 定义response输出类型为image/jpeg类型
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // 设置IE扩展的HTTP / 1.1无缓存标头（使用addHeader）。
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // 设置标准的HTTP / 1.0无缓存标头。
        response.setHeader("Pragma", "no-cache");
        // 返回一张图片
        response.setContentType("image/jpeg");
        //-------------------生成验证码 begin --------------------------
        //获取验证码文本内容
        String text = defaultKaptcha.createText();
        //将验证码文本内容放入session
        request.getSession().setAttribute("captcha", text);
        //根据文本验证码内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //输出流输出图片，格式为jpg
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //-------------------生成验证码 end --------------------------
    }

    @ApiOperation(value = "注册时发送邮件验证码")
    @PostMapping(value = "/emailCode")
    public Result emailCode(@RequestBody RegisterUser user) {
        if(ObjectUtil.isEmpty(user) && StrUtil.isBlank(user.getEmail()) && StrUtil.isBlank(user.getVerifyCode())) {
            return Result.fail(ResultCodeEnum.ISNULL);
        }
        try {
            // 判断是否已经注册
            if(userService.checkUserEmail(user.getEmail())){
                return Result.fail("当前邮箱已经被注册!");
            }
            // 发送邮件前校验验证码

            // 发送验证码
            String verifyCode = emailUtil.sendCodeMessage(user);
            cacheService.setString(user.getUserName() + "_verifyCode",verifyCode,180);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("邮箱验证码发送失败！");
        }
        return Result.ok(ResultCodeEnum.VERIFY_CODE_SUCCESS);
    }
}
