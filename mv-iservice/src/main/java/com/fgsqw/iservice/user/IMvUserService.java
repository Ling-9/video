package com.fgsqw.iservice.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.beans.user.RegisterUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ling
 * @since 2022-10-08
 */
public interface IMvUserService extends IService<MvUser> {

    Result regMvUser(RegisterUser user);

    Boolean checkUserName(String username);

    Boolean checkUserEmail(String email);

    MvUser getMvUserByUserName(String username);
}
