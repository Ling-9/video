package com.fgsqw.iserviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.dao.user.MvUserMapper;
import com.fgsqw.iservice.IMvUserService;
import org.springframework.stereotype.Service;

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

}
