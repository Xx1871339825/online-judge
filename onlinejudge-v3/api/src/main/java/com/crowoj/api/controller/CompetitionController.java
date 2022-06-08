package com.crowoj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.dto.CompetitionDTO;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.dto.CinfoDTO;
import com.crowoj.api.entity.User;
import com.crowoj.api.param.RegisterCompetitionParam;
import com.crowoj.api.service.CompetitionService;
import com.crowoj.api.vo.CompetitionRank;
import com.crowoj.api.vo.ResultVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/18 23:05
 * @description
 */
@RestController("apiCompetitionController")
@RequestMapping("api/competition")
public class CompetitionController {
    @Resource
    private CompetitionService competitionService;
    @GetMapping("list")
    public ResultVO<Page<CompetitionDTO>> getCompetitionList(
            String search,Integer status,Long uid,Page<CompetitionDTO> page
    ){
        if (search == null) search = "";
        if (status == null) status = -1;
        if (uid == null) uid = -1L;
        return ResultUtil.success(competitionService.search(search,status,-1,uid,page));
    }

    @GetMapping("valid/{cid}/{pwd}")
    public ResultVO<Boolean> validPwd(@PathVariable Long cid, @PathVariable String pwd){
        return ResultUtil.success(competitionService.valid(cid,pwd));
    }

    @PostMapping("register")
    public ResultVO<Boolean> register(@RequestBody@Valid RegisterCompetitionParam param){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(competitionService.register(activityUser.getUser(),param));
    }

    @GetMapping("{cid}")
    public ResultVO<CinfoDTO> getCompetitionInfoDTO(@PathVariable Long cid){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(competitionService.getCinfoDTO(activityUser.getUser(),cid));
    }


    @GetMapping("desc/{id}")
    public ResultVO<String> getCompetitionDesc(@PathVariable Long id){
        return ResultUtil.success(competitionService.getById(id).getDescription());
    }

    @GetMapping("problem/{cid}")
    public ResultVO<List<ProblemDTO>> getProblemListByCid(@PathVariable Long cid){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(competitionService.getProblemListByCid(activityUser,cid));
    }

    @GetMapping("problem/info/{cid}/{pid}")
    public ResultVO<ProblemDTO> getCompetitionProblemInfo(@PathVariable Long cid,@PathVariable Long pid){
        var activityUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        //判断是否报名且比赛是否开始
        return ResultUtil.success(competitionService.getCompetitionProblemInfo(activityUser.getUser(),cid,pid));
    }

    @GetMapping("rank/{cid}")
    public ResultVO<List<CompetitionRank>> getRankList(@PathVariable Long cid){
        return ResultUtil.success(competitionService.getRankList(cid));
    }


}
