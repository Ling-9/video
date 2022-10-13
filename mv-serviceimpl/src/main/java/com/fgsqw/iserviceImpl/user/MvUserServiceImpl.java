package com.fgsqw.iserviceImpl.user;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgsqw.JwtUtil;
import com.fgsqw.beans.exception.MyRuntimeException;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.result.ResultCodeEnum;
import com.fgsqw.beans.user.LoginUser;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.beans.user.QueryUser;
import com.fgsqw.beans.user.RegLogUser;
import com.fgsqw.dao.user.MvUserMapper;
import com.fgsqw.iservice.redis.IRedisCacheService;
import com.fgsqw.iservice.user.IMvUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private IRedisCacheService cacheService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Result<Map<String, Object>> login(RegLogUser user, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        String verifyCode = user.getVerifyCode();
        if(StrUtil.isEmpty(verifyCode) && !StrUtil.equals(captcha,verifyCode)){
            return Result.fail("验证码错误,请重新输入!");
        }
        // 用户认证
        LoginUser userDetails = (LoginUser)userDetailsService.loadUserByUsername(user.getUserName());
        if(ObjectUtil.isNull(userDetails) || passwordEncoder.matches(userDetails.getPassword(),user.getPasswd())){
            return Result.fail("用户名或密码错误!");
        }
        if(userDetails.isEnabled()){
            return Result.fail("账号被禁用!");
        }
        // 更新security 登录用户
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 认证通过 生成jwt
        String token = jwtUtil.generateToken(userDetails);

        // 对象信息存入redis
        JSONObject userInfo = new JSONObject();
        userInfo.set("loginUserId",userDetails.getSid());
        userInfo.set("loginUserName",userDetails.getUsername());
        userInfo.set("loginPhoneNum",userDetails.getPhone());
        userInfo.set("loginEmail",userDetails.getEmail());
        userInfo.set("loginSex",userDetails.getSex());
        userInfo.set("loginHead",userDetails.getHead());
        userInfo.set("loginNickName",userDetails.getNickName());
        cacheService.setObject(token,userInfo);

        HashMap<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return Result.ok("登录成功!",tokenMap);
    }

    @Override
    public Result registerUser(RegLogUser user) {
        String userName = user.getUserName();
        String passwd = user.getPasswd();
        String email = user.getEmail();
        String verifyCode = user.getVerifyCode();
        String phone = user.getPhone();

        if(StrUtil.isAllBlank(userName,passwd,email,verifyCode)){
            return Result.fail(ResultCodeEnum.ISNULL);
        }
        // 密码加密
        String encode = new BCryptPasswordEncoder().encode(passwd);
        user.setPasswd(encode);
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

    @Override
    public MvUser getMvUserByUserName(String username) {
        return this.getOne(Wrappers.<MvUser>lambdaQuery()
                .eq(MvUser::getUserName,username)
                .eq(MvUser::getStatus,0)
                .eq(MvUser::getDelFlag,0));
    }

    @Override
    public Result<List<MvUser>> queryUser(QueryUser user) {
        //构造查询条件
        LambdaQueryWrapper<MvUser> query = queryWrapper(user);
        Integer page = user.getPage();
        Integer limit = user.getLimit();
        page = ObjectUtils.isNull(page) ? 1 : page;
        limit = ObjectUtils.isNull(limit) ? 10 : limit;

        Page<MvUser> userPage = page(new Page<>(page,limit),query);
        return Result.ok(userPage.getRecords(),userPage.getTotal());
    }

    @Override
    public Result<Map<String, String>> queryInfo(Principal principal) {
        HashMap<String, String> map = new HashMap<>();
        String userName = principal.getName();
        MvUser mvUser = this.getMvUserByUserName(userName);
        if(ObjectUtil.isEmpty(mvUser)){
            throw new MyRuntimeException("没有查询到对应用户信息!");
        }
        map.put("username",mvUser.getUserName());
        map.put("sex",mvUser.getSex().toString());
        map.put("head",mvUser.getHead());
        map.put("status",mvUser.getStatus().toString());
        map.put("email", mvUser.getEmail());
        map.put("phone",mvUser.getPhone());
        return Result.ok(map);
    }

    private MvUser creUser(RegLogUser user) {
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

    private LambdaQueryWrapper<MvUser> queryWrapper(QueryUser user) {
        LambdaQueryWrapper<MvUser> query = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(user.getUserName())) {
            query.eq(MvUser::getUserName, user.getUserName());
        }
        if (StrUtil.isNotBlank(user.getEmail())) {
            query.eq(MvUser::getEmail, user.getEmail());
        }
        if (StrUtil.isNotBlank(user.getPhone())) {
            query.eq(MvUser::getPhone, user.getPhone());
        }
        if (StrUtil.isNotBlank(user.getStatus())) {
            query.eq(MvUser::getStatus, user.getStatus());
        }
        return query;
    }
}
