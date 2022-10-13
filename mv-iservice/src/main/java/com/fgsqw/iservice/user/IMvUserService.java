package com.fgsqw.iservice.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fgsqw.beans.result.Result;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.beans.user.QueryUser;
import com.fgsqw.beans.user.RegLogUser;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ling
 * @since 2022-10-08
 */
public interface IMvUserService extends IService<MvUser> {

    Result<Map<String, Object>> login(RegLogUser user, HttpServletRequest request);

    Result registerUser(RegLogUser user);

    Boolean checkUserName(String username);

    Boolean checkUserEmail(String email);

    MvUser getMvUserByUserName(String username);

    Result<List<MvUser>> queryUser(QueryUser user);

    Result<Map<String, String>> queryInfo(Principal principal);
}
