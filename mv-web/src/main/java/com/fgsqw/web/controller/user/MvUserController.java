package com.fgsqw.web.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.beans.user.RegLogUser;
import com.fgsqw.iservice.user.IMvUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")

public class MvUserController {

    @Autowired
    IMvUserService userService;

    @GetMapping("getUserByName")
    public Result<List<MvUser>> getUserByName(String username){
        MvUser user = userService.getOne(new LambdaQueryWrapper<MvUser>().eq(MvUser::getUserName, username));
        List<MvUser> list = userService.list(new LambdaQueryWrapper<MvUser>().eq(MvUser::getUserName, username));
        Result<List<MvUser>> ok = Result.ok(list);
        Result.fail();
        return Result.ok(list);
    }

    // 验证码登录,登录后返回Token
    @PostMapping("login")
    public Result login(@RequestBody RegLogUser user){
        try {
            return userService.login(user);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("登录失败！");
        }
    }

    @GetMapping("logout")
    public Result logout(){
        return null;
    }

    @PostMapping("register")
    public Result registerUser(@RequestBody RegLogUser user){
        try {
            return userService.registerUser(user);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("注册失败！");
        }
    }

}
