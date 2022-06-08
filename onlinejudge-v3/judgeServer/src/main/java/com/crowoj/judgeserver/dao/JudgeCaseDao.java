package com.crowoj.judgeserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowoj.judgeserver.entity.JudgeCase;
import org.apache.ibatis.annotations.Mapper;

/**
 * (JudgeCase)表数据库访问层
 *
 * @author crow
 * @since 2022-02-22 23:57:10
 */
@Mapper
public interface JudgeCaseDao extends BaseMapper<JudgeCase> {

}

