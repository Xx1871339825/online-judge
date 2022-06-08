package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.CompetitionRegisterDao;
import com.crowoj.api.entity.CompetitionRegister;
import com.crowoj.api.service.CompetitionRegisterService;
import org.springframework.stereotype.Service;

/**
 * (CompetitionRegister)表服务实现类
 *
 * @author crow
 * @since 2022-03-06 21:17:57
 */
@Service("competitionRegisterService")
public class CompetitionRegisterServiceImpl extends ServiceImpl<CompetitionRegisterDao, CompetitionRegister> implements CompetitionRegisterService {

}

