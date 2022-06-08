package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.entity.JudgeCase;
import com.crowoj.api.vo.JudgeCaseVO;

import java.util.List;

/**
 * (JudgeCase)表服务接口
 *
 * @author crow
 * @since 2022-03-01 19:00:12
 */
public interface JudgeCaseService extends IService<JudgeCase> {
    List<JudgeCaseVO> getJudgeCaseVOList(Long sid,int type);
}

