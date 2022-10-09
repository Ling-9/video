package com.fgsqw.web.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.result.ResultCodeEnum;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.iservice.IMvUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping("Registration")
    public Result regMvUser(@RequestBody MvUser user){
        if(ObjectUtil.isEmpty(user)){
            return Result.fail(ResultCodeEnum.ISNULL);
        }
        //redis 验证码校验
        creUser(user);
        try {
            userService.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("注册失败！");
        }
        return Result.ok();
    }

    private void creUser(MvUser user) {
        user.setIsVip(false);
        user.setStatus(0);
        user.setPerm(0);
        user.setCreateTime(LocalDateTime.now());
        user.setDelFlag(false);
    }
}
