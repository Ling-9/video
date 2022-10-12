package com.fgsqw.web.controller.user;

import cn.hutool.core.util.ObjectUtil;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.beans.user.QueryUser;
import com.fgsqw.beans.user.RegLogUser;
import com.fgsqw.iservice.user.IMvUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")

public class MvUserController {

    @Autowired
    IMvUserService userService;

    // 验证码登录,登录后返回Token
    @ApiOperation(value = "登录之后返回Token")
    @PostMapping("login")
    public Result<Map<String, Object>> login(@RequestBody RegLogUser user,HttpServletRequest request){
        try {
            return userService.login(user,request);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("登录失败！");
        }
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("logout")
    public Result logout(){
        return null;
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("info")
    public Result<MvUser> getUserInfo(Principal principal){
        if(ObjectUtil.isEmpty(principal)){
            return Result.ok();
        }
        String name = principal.getName();
        MvUser user = userService.getMvUserByUserName(name);
        user.setPasswd(null);
        return Result.ok(user);
    }

    @ApiOperation(value = "注册用户")
    @PostMapping("register")
    public Result registerUser(@RequestBody RegLogUser user){
        try {
            return userService.registerUser(user);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("注册失败！");
        }
    }

    @ApiOperation(value = "根据条件查询User")
    @PostMapping("query")
    public Result<List<MvUser>> queryUser(@RequestBody QueryUser user){
        try {
            return userService.queryUser(user);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("查询用户失败!");
        }
    }
}
