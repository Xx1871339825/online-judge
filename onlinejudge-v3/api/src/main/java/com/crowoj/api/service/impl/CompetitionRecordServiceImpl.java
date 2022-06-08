package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.CompetitionRecordDao;
import com.crowoj.api.entity.CompetitionRecord;
import com.crowoj.api.service.CompetitionRecordService;
import org.springframework.stereotype.Service;

/**
 * (CompetitionRecord)表服务实现类
 *
 * @author crow
 * @since 2022-03-06 21:17:57
 */
@Service("competitionRecordService")
public class CompetitionRecordServiceImpl extends ServiceImpl<CompetitionRecordDao, CompetitionRecord> implements CompetitionRecordService {

}

