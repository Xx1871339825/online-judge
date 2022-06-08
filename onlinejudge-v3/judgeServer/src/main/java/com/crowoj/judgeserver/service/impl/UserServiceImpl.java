package com.crowoj.judgeserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.judgeserver.dao.UserDao;
import com.crowoj.judgeserver.entity.User;
import com.crowoj.judgeserver.service.UserService;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author crow
 * @since 2022-02-28 18:27:41
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

}

