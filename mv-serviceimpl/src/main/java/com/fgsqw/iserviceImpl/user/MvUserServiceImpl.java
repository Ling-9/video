package com.fgsqw.iserviceImpl.user;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.result.ResultCodeEnum;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.beans.user.RegisterUser;
import com.fgsqw.dao.user.MvUserMapper;
import com.fgsqw.iservice.redis.IRedisCacheService;
import com.fgsqw.iservice.user.IMvUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ling
 * @since 2022-10-08
 */
@Service
public class MvUserServiceImpl extends ServiceImpl<MvUserMapper, MvUser> implements IMvUserService {

    @Autowired
    IRedisCacheService cacheService;

    @Override
    public Result regMvUser(RegisterUser user) {
        String userName = user.getUserName();
        String passwd = user.getPasswd();
        String email = user.getEmail();
        String verifyCode = user.getVerifyCode();
        String phone = user.getPhone();

        if(StrUtil.isAllBlank(userName,passwd,email,verifyCode)){
            return Result.fail(ResultCodeEnum.ISNULL);
        }
        // 密码加密
//        String encode = new BCryptPasswordEncoder().encode(passwd);
//        user.setPasswd(encode);
        // 校验邮箱验证码
        String cacheVerify = cacheService.getString(userName + "_verifyCode");
        if(StrUtil.isBlank(cacheVerify)){
            return Result.fail(ResultCodeEnum.VERIFY_CODE_INVALID);
        }else if(!(StrUtil.equals(verifyCode,cacheVerify))) {
            return Result.fail(ResultCodeEnum.VERIFY_CODE_FAIL);
        }
        // 判断是否已经存在账号(邮箱/手机号)
        if(checkUserEmail(email)){
            return Result.fail("账号已存在" + email);
        }
        this.save(creUser(user));
        return Result.ok("注册成功!");
    }

    @Override
    public Boolean checkUserName(String username) {
        MvUser user = this.getOne(Wrappers.<MvUser>lambdaQuery().eq(MvUser::getUserName, username));
        return Objects.nonNull(user);
    }

    @Override
    public Boolean checkUserEmail(String email) {
        MvUser user = this.getOne(Wrappers.<MvUser>lambdaQuery().eq(MvUser::getEmail, email));
        return Objects.nonNull(user);
    }

    private MvUser creUser(RegisterUser user) {
        MvUser mvUser = new MvUser();
        BeanUtils.copyProperties(user,mvUser);
        mvUser.setSid(IdUtil.getSnowflakeNextId());
        mvUser.setIsVip(false);
        mvUser.setStatus(0);
        mvUser.setPerm(0);
        mvUser.setCreateTime(LocalDateTime.now());
        mvUser.setDelFlag(false);
        return mvUser;
    }

    private LambdaQueryWrapper<MvUser> queryWrapper(MvUser mvUser) {
        LambdaQueryWrapper<MvUser> query = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(mvUser.getUserName())) {
            query.eq(MvUser::getUserName, mvUser.getUserName());
        }
        return query;
    }
}
