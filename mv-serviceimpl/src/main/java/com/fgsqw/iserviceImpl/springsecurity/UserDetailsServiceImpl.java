package com.fgsqw.iserviceImpl.springsecurity;

import cn.hutool.core.util.ObjectUtil;
import com.fgsqw.beans.user.LoginUser;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.iservice.user.IMvUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    IMvUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MvUser mvUser = userService.getMvUserByUserName(username);
        if(ObjectUtil.isEmpty(mvUser)){
            throw new RuntimeException("用户名或密码错误");
        }
        return new LoginUser(mvUser);
    }
}
