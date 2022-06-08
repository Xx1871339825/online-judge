package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowoj.api.entity.JudgeCase;
import com.crowoj.api.vo.JudgeCaseVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (JudgeCase)表数据库访问层
 *
 * @author crow
 * @since 2022-03-01 19:00:12
 */
public interface JudgeCaseDao extends BaseMapper<JudgeCase> {

    @Select("""
            SELECT jc.*,pc.stdin,pc.stdout
            FROM judge_case AS jc
            		LEFT JOIN
            			problem_case AS pc
            			ON pc.pid = jc.pid
            			AND pc.id = jc.case_id
            WHERE
            	jc.submit_id = #{sid}
            """)
    List<JudgeCaseVO> getJudgeCaseVOList(Long sid);
}

