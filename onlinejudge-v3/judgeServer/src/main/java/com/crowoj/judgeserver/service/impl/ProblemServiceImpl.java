package com.crowoj.judgeserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.judgeserver.dao.ProblemDao;
import com.crowoj.judgeserver.entity.Problem;
import com.crowoj.judgeserver.service.ProblemService;
import org.springframework.stereotype.Service;

/**
 * (Problem)表服务实现类
 *
 * @author crow
 * @since 2022-02-22 23:31:55
 */
@Service("problemService")
public class ProblemServiceImpl extends ServiceImpl<ProblemDao, Problem> implements ProblemService {

}

