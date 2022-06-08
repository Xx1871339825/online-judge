package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.ProblemTagDao;
import com.crowoj.api.entity.ProblemTag;
import com.crowoj.api.service.ProblemTagService;
import org.springframework.stereotype.Service;

/**
 * (ProblemTag)表服务实现类
 *
 * @author crow
 * @since 2022-03-12 15:30:34
 */
@Service("problemTagService")
public class ProblemTagServiceImpl extends ServiceImpl<ProblemTagDao, ProblemTag> implements ProblemTagService {

}

