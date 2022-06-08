package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.core.enums.ProblemType;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.entity.Problem;
import com.crowoj.api.param.AddProblemParam;

import java.util.List;

/**
 * @author crow
 * @create 2022/2/20 22:25
 * @description
 */
public interface ProblemService extends IService<Problem> {

    ProblemDTO getProblemById(Long pid, Long uid, ProblemType problemType);

    Page<ProblemDTO>search(Long uid, String search,ProblemType problemType , Page<ProblemDTO> page);

    Boolean saveOrUpdateProblem(ActivityUser activityUser, AddProblemParam param);

    AddProblemParam getProblemInfo(ActivityUser activityUser, Long pid);

    List<ProblemDTO> getSearchProblemList(String search);
}
