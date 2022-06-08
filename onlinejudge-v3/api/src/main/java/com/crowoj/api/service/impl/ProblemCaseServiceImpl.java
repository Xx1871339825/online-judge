package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.ProblemCaseDao;
import com.crowoj.api.entity.ProblemCase;
import com.crowoj.api.service.ProblemCaseService;
import org.springframework.stereotype.Service;

/**
 * (ProblemCase)表服务实现类
 *
 * @author crow
 * @since 2022-03-13 14:00:28
 */
@Service("problemCaseService")
public class ProblemCaseServiceImpl extends ServiceImpl<ProblemCaseDao, ProblemCase> implements ProblemCaseService {

}

