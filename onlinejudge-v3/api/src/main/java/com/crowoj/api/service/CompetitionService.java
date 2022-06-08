package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.dto.CompetitionDTO;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.dto.CinfoDTO;
import com.crowoj.api.entity.Competition;
import com.crowoj.api.entity.User;
import com.crowoj.api.param.AddCompetitionParam;
import com.crowoj.api.param.RegisterCompetitionParam;
import com.crowoj.api.param.UpdateCompetitionParam;
import com.crowoj.api.vo.CompetitionRank;

import java.util.List;

/**
 * (Competition)表服务接口
 *
 * @author crow
 * @since 2022-03-18 14:20:34
 */
public interface CompetitionService extends IService<Competition> {

    Boolean addCompetition(ActivityUser activityUser, AddCompetitionParam param);

    Page<CompetitionDTO> search(String search, Integer status, Integer type,Long uid, Page<CompetitionDTO> page);

    Boolean valid(Long cid, String pwd);

    List<ProblemDTO> getProblemListByCid(ActivityUser activityUser,Long cid);

    Boolean register(User user, RegisterCompetitionParam param);

    CinfoDTO getCinfoDTO(User user, Long cid);

    ProblemDTO getCompetitionProblemInfo(User user,Long cid, Long pid);

    List<CompetitionRank> getRankList(Long cid);

    Boolean deleteById(Long cid);

    Boolean updateCompetition(UpdateCompetitionParam param);

    List<ProblemDTO> getProblemListByCid(Long cid);
}

