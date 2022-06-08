package com.crowoj.judgeserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.judgeserver.dao.UserSubmitDao;
import com.crowoj.judgeserver.entity.UserSubmit;
import com.crowoj.judgeserver.service.UserSubmitService;
import org.springframework.stereotype.Service;

/**
 * (UserSubmit)表服务实现类
 *
 * @author crow
 * @since 2022-02-22 23:57:10
 */
@Service("userSubmitService")
public class UserSubmitServiceImpl extends ServiceImpl<UserSubmitDao, UserSubmit> implements UserSubmitService {

}

