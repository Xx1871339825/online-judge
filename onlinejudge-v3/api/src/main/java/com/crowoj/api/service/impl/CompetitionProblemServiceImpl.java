package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.CompetitionProblemDao;
import com.crowoj.api.entity.CompetitionProblem;
import com.crowoj.api.service.CompetitionProblemService;
import org.springframework.stereotype.Service;

/**
 * (CompetitionProblem)表服务实现类
 *
 * @author crow
 * @since 2022-03-06 21:17:57
 */
@Service("competitionProblemService")
public class CompetitionProblemServiceImpl extends ServiceImpl<CompetitionProblemDao, CompetitionProblem> implements CompetitionProblemService {

}

