package com.crowoj.judgeserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowoj.judgeserver.entity.Problem;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Problem)表数据库访问层
 *
 * @author crow
 * @since 2022-02-22 23:31:53
 */
@Mapper
public interface ProblemDao extends BaseMapper<Problem> {

}

