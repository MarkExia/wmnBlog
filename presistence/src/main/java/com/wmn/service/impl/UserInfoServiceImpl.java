package com.wmn.service.impl;

import com.wmn.entity.UserInfo;
import com.wmn.mapper.UserInfoMapper;
import com.wmn.service.IUserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wmn
 * @since 2019-09-25
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
