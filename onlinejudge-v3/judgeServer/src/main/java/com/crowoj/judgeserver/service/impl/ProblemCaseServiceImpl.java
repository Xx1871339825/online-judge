package com.crowoj.judgeserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.judgeserver.dao.ProblemCaseDao;
import com.crowoj.judgeserver.entity.ProblemCase;
import com.crowoj.judgeserver.service.ProblemCaseService;
import org.springframework.stereotype.Service;

/**
 * (ProblemCase)表服务实现类
 *
 * @author crow
 * @since 2022-02-22 23:57:10
 */
@Service("problemCaseService")
public class ProblemCaseServiceImpl extends ServiceImpl<ProblemCaseDao, ProblemCase> implements ProblemCaseService {

}

