package com.fgsqw.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.iservice.IMvUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
