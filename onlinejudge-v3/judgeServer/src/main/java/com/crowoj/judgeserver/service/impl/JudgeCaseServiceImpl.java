package com.crowoj.judgeserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.judgeserver.dao.JudgeCaseDao;
import com.crowoj.judgeserver.entity.JudgeCase;
import com.crowoj.judgeserver.service.JudgeCaseService;
import org.springframework.stereotype.Service;

/**
 * (JudgeCase)表服务实现类
 *
 * @author crow
 * @since 2022-02-22 23:57:10
 */
@Service("judgeCaseService")
public class JudgeCaseServiceImpl extends ServiceImpl<JudgeCaseDao, JudgeCase> implements JudgeCaseService {

}

